package com.payswiff.mfmsproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.models.Feedback;
import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.Device;
import com.payswiff.mfmsproject.models.Merchant;
import com.payswiff.mfmsproject.models.Question; // Import the Question model
import com.payswiff.mfmsproject.models.FeedbackQuestionsAssociation; // Import the association model
import com.payswiff.mfmsproject.repositories.FeedbackRepository;
import com.payswiff.mfmsproject.repositories.EmployeeRepository;
import com.payswiff.mfmsproject.repositories.DeviceRepository;
import com.payswiff.mfmsproject.repositories.MerchantRepository;
import com.payswiff.mfmsproject.repositories.QuestionRepository; // Import the Question repository
import com.payswiff.mfmsproject.reuquests.CreateFeedbackRequest;
import com.payswiff.mfmsproject.dtos.AverageRatingResponseDTO;
import com.payswiff.mfmsproject.dtos.DeviceFeedbackCountDTO;
import com.payswiff.mfmsproject.dtos.EmailSendDto;
import com.payswiff.mfmsproject.dtos.EmployeeFeedbackCountDto;
import com.payswiff.mfmsproject.dtos.FeedbackQuestionAnswerAssignDto;
import com.payswiff.mfmsproject.exceptions.MerchantDeviceNotAssignedException;
import com.payswiff.mfmsproject.exceptions.ResourceAlreadyExists;
import com.payswiff.mfmsproject.exceptions.ResourceNotFoundException;
import com.payswiff.mfmsproject.exceptions.ResourceUnableToCreate;
import com.payswiff.mfmsproject.exceptions.UnableSentEmail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
/**
 * feedback service class
 * create feedback
 * get feedbacks
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 * */
@Service
public class FeedbackService {

	private static final Logger logger = LogManager.getLogger(FeedbackService.class); // Logger initialization

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private QuestionRepository questionRepository; // Inject Question repository

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private FeedbackQuestionsAssociationService feedbackQuestionsAssociationService; // Inject the association service

	@Autowired
	private MerchantDeviceAssociationService merchantDeviceAssociationService;

	@Autowired
	private EmailService emailService;

	/**
	 * Method to create feedback if all associated entities are available.
	 *
	 * @param feedbackRequest The request object containing feedback data.
	 * @param questionAnswers List of feedback question answers to associate with
	 *                        the feedback.
	 * @return true if feedback creation is successful.
	 * @throws Exception
	 */
	public boolean createFeedback(CreateFeedbackRequest feedbackRequest,
			List<FeedbackQuestionAnswerAssignDto> questionAnswers) throws Exception {

		try {
			logger.info("Creating feedback for Employee ID: " + feedbackRequest.getFeedbackEmployeeId()
					+ ", Merchant ID: " + feedbackRequest.getFeedbackMerchantId() + ", Device ID: "
					+ feedbackRequest.getFeedbackDeviceId());

// Check if the employee exists
			Optional<Employee> employeeOptional = employeeRepository.findById(feedbackRequest.getFeedbackEmployeeId());
			if (employeeOptional.isEmpty()) {
				logger.error("Employee not found with ID: " + feedbackRequest.getFeedbackEmployeeId());
				throw new ResourceNotFoundException("Employee", "ID",
						feedbackRequest.getFeedbackEmployeeId().toString());
			}
			Employee employee = employeeOptional.get();
			logger.info("Found employee: " + employee.getEmployeeId());

// Check if the merchant exists
			Optional<Merchant> merchantOptional = merchantRepository.findById(feedbackRequest.getFeedbackMerchantId());
			if (merchantOptional.isEmpty()) {
				logger.error("Merchant not found with ID: " + feedbackRequest.getFeedbackMerchantId());
				throw new ResourceNotFoundException("Merchant", "ID",
						feedbackRequest.getFeedbackMerchantId().toString());
			}
			Merchant merchant = merchantOptional.get();
			logger.info("Found merchant: " + merchant.getMerchantId());

// Check if the device exists
			Optional<Device> deviceOptional = deviceRepository.findById(feedbackRequest.getFeedbackDeviceId());
			if (deviceOptional.isEmpty()) {
				logger.error("Device not found with ID: " + feedbackRequest.getFeedbackDeviceId());
				throw new ResourceNotFoundException("Device", "ID", feedbackRequest.getFeedbackDeviceId().toString());
			}
			Device device = deviceOptional.get();
			logger.info("Found device: " + device.getDeviceId());

// Check whether the merchant has the given device or not
			if (!merchantDeviceAssociationService.isDeviceAssociatedWithMerchant(
					feedbackRequest.getFeedbackMerchantId(), feedbackRequest.getFeedbackDeviceId())) {
				logger.error("Device with ID " + feedbackRequest.getFeedbackDeviceId()
						+ " is not associated with Merchant ID " + feedbackRequest.getFeedbackMerchantId());
				throw new MerchantDeviceNotAssignedException("Device", "ID",
						String.valueOf(feedbackRequest.getFeedbackDeviceId()), "Merchant", "ID",
						String.valueOf(feedbackRequest.getFeedbackMerchantId()));
			}

// If all checks pass, create feedback
			Feedback feedback = new Feedback();
			feedback.setFeedbackEmployee(employee);
			feedback.setFeedbackDevice(device);
			feedback.setFeedbackMerchant(merchant);
			feedback.setFeedbackRating(feedbackRequest.getFeedbackRating());
			feedback.setFeedbackUuid(UUID.randomUUID().toString());
			feedback.setFeedbackImage1(feedbackRequest.getFeedbackImage1());
			feedback.setFeedback(feedbackRequest.getFeedback());

// Save feedback to the repository
			Feedback savedFeedback = feedbackRepository.save(feedback);
			logger.info("Feedback created with ID: " + savedFeedback.getFeedbackId());

// Associate questions with created feedback
			associateFeedbackWithQuestions(savedFeedback, questionAnswers);
			logger.info("Feedback with ID: " + savedFeedback.getFeedbackId() + " associated with questions.");

// Send email to employee for feedback status and feedback details
			sendSuccessEmail(employee, savedFeedback);
			logger.info("Success email sent to Employee ID: " + employee.getEmployeeId());

			return true;

		} catch (ResourceNotFoundException | MerchantDeviceNotAssignedException | UnableSentEmail e) {
// Send failure email to inform about the error
			Optional<Employee> employeeOptional = employeeRepository.findById(feedbackRequest.getFeedbackEmployeeId());
			if (employeeOptional.isEmpty()) {
				logger.error("Employee not found with ID: " + feedbackRequest.getFeedbackEmployeeId());
				throw new ResourceNotFoundException("Employee", "ID",
						feedbackRequest.getFeedbackEmployeeId().toString());
			}
			Employee employee = employeeOptional.get();
			sendFailureEmail(feedbackRequest, employee, e);
			logger.error("Failure email sent to Employee ID: " + employee.getEmployeeId() + " due to exception: "
					+ e.getMessage());

			// the exception to maintain the original behavior
			throw e;
		}
	}

	/**
	 * Method to send a success email to the employee once feedback is successfully
	 * created.
	 *
	 * @param employee The employee to send the email to.
	 * @param feedback The feedback details to include in the email.
	 */
	private void sendSuccessEmail(Employee employee, Feedback feedback) throws UnableSentEmail {
		// set the log
		logger.info("Initiating sending feedback creation success mail to :{}", employee.getEmployeeEmail());
		// create emailDto object
		EmailSendDto emailSendDto = new EmailSendDto();
		// to mail
		emailSendDto.setTo(employee.getEmployeeEmail());
		// subject
		emailSendDto.setSubject("Merchant Feedback Management System");
		// email content or body
		String emailContent = "Feedback Creation Status: Created\n" + "\n"
				+ "---------------------------------------------------\n" + "Feedback Details\n"
				+ "---------------------------------------------------\n" + "Feedback ID: " + feedback.getFeedbackId()
				+ "\n" + "Feedback UUID: " + feedback.getFeedbackUuid() + "\n" + "Employee: "
				+ feedback.getFeedbackEmployee().getEmployeeName() + "\n" + "Feedback Rating: "
				+ feedback.getFeedbackRating() + "\n" + "Feedback: " + feedback.getFeedback() + "\n"
				+ "Feedback Image: " + (feedback.getFeedbackImage1() != null ? "Available" : "Not Provided") + "\n"
				+ "\n" + "---------------------------------------------------\n" + "Device Details\n"
				+ "---------------------------------------------------\n" + "Device ID: "
				+ feedback.getFeedbackDevice().getDeviceId() + "\n" + "Device UUID: "
				+ feedback.getFeedbackDevice().getDeviceUuid() + "\n" + "Device Model: "
				+ feedback.getFeedbackDevice().getDeviceModel() + "\n" + "Device Manufacturer: "
				+ feedback.getFeedbackDevice().getDeviceManufacturer() + "\n" + "\n"
				+ "---------------------------------------------------\n" + "Merchant Details\n"
				+ "---------------------------------------------------\n" + "Merchant UUID: "
				+ feedback.getFeedbackMerchant().getMerchantUuid() + "\n" + "Merchant Email: "
				+ feedback.getFeedbackMerchant().getMerchantEmail() + "\n" + "Merchant Phone: "
				+ feedback.getFeedbackMerchant().getMerchantPhone() + "\n" + "Merchant Business Name: "
				+ feedback.getFeedbackMerchant().getMerchantBusinessName() + "\n" + "Merchant Business Type: "
				+ feedback.getFeedbackMerchant().getMerchantBusinessType() + "\n";
		// assign text or body
		emailSendDto.setText(emailContent);
		// send email
		boolean emailSent = emailService.sendEmail(emailSendDto.getTo(), emailSendDto.getSubject(),
				emailSendDto.getText());
		logger.info("feedback creation success Email sent successfully to: ", employee.getEmployeeEmail());

		if (!emailSent) {
			logger.info("Unable to send feedback creation success  email to: ", employee.getEmployeeEmail());

			throw new UnableSentEmail(employee.getEmployeeEmail());
		}
	}

	/**
	 * Method to send a failure email in case feedback creation fails.
	 *
	 * @param feedbackRequest The feedback request object.
	 * @param employee
	 * @param e               The exception that occurred.
	 * @throws Exception
	 */
	private void sendFailureEmail(CreateFeedbackRequest feedbackRequest, Employee employee, Exception e)

			throws Exception {
		// log email
		logger.info("Initiating sending feedback creation failure mail to :{}", employee.getEmployeeEmail());
		// dto
		EmailSendDto emailSendDto = new EmailSendDto();
		// to mail
		emailSendDto.setTo(employee.getEmployeeEmail()); // Send email to admin or appropriate address
		emailSendDto.setSubject("Feedback Creation Failed");// subject

		String emailContent = "Dear Admin,\n\n" + "We encountered an error while creating feedback.\n" + "Error: "
				+ e.getClass().getSimpleName() + "\n" + "Details: " + e.getMessage() + "\n\n"
				+ "Feedback Request Details:\n" + "Employee ID: " + feedbackRequest.getFeedbackEmployeeId() + "\n"
				+ "Merchant ID: " + feedbackRequest.getFeedbackMerchantId() + "\n" + "Device ID: "
				+ feedbackRequest.getFeedbackDeviceId() + "\n" + "Rating: " + feedbackRequest.getFeedbackRating()
				+ "\n";

		emailSendDto.setText(emailContent);// text or body

		try {
			logger.info("feedback creation failure Email sent successfully to : {}", employee.getEmployeeEmail());

			emailService.sendEmail(emailSendDto.getTo(), emailSendDto.getSubject(), emailSendDto.getText());
		} catch (Exception emailException) {
			// Log the error if email fails to send
			logger.info("feedback creation failure Email to : {}", employee.getEmployeeEmail());

			throw new Exception("failed to send email: " + emailException.getMessage());
		}
	}

	/**
	 * Method to associate feedback with predefined questions and their answers.
	 *
	 * @param feedback        The feedback object to associate with questions.
	 * @param questionAnswers
	 * @throws ResourceNotFoundException
	 * @throws ResourceUnableToCreate
	 * @throws ResourceAlreadyExists
	 */
	private void associateFeedbackWithQuestions(Feedback feedback,
			List<FeedbackQuestionAnswerAssignDto> questionAnswers)
			throws ResourceNotFoundException, ResourceUnableToCreate {

		logger.info("Starting to associate feedback with questions for feedback ID: " + feedback.getFeedbackId());

		// Fetch the predefined questions from the database
		logger.debug("Fetching predefined questions from the database.");
		List<Question> predefinedQuestions = questionRepository.findAll();
		logger.debug("Retrieved " + predefinedQuestions.size() + " predefined questions from the database.");

		// Convert the list of questionAnswers to a map for faster lookups (questionId
		// -> questionAnswer)
		logger.debug("Converting question answers to a map for fast lookups.");
		Map<Long, String> questionAnswerMap = questionAnswers.stream().collect(Collectors.toMap(
				FeedbackQuestionAnswerAssignDto::getQuestionId, FeedbackQuestionAnswerAssignDto::getQuestionAnswer));
		logger.debug("Converted question answers to map with " + questionAnswerMap.size() + " entries.");

		// Iterate over the predefined questions
		for (Question question : predefinedQuestions) {
			// Get the answer for the current question from the map, if it exists
			String answerForQuestion = questionAnswerMap.getOrDefault(question.getQuestionId(), "No answer provided");

			// Log the question and the answer
			logger.debug("Associating feedback ID: " + feedback.getFeedbackId() + " with question ID: "
					+ question.getQuestionId() + " and answer: " + answerForQuestion);

			// Create the association and save it
			FeedbackQuestionsAssociation association = new FeedbackQuestionsAssociation();
			association.setFeedback(feedback); // Set the feedback object
			association.setQuestion(question); // Set the question object
			association.setAnswer(answerForQuestion); // Set the actual answer or default value

			// Save the association using the association service
			try {
				logger.debug("Saving the association for feedback ID: " + feedback.getFeedbackId()
						+ " and question ID: " + question.getQuestionId());
				feedbackQuestionsAssociationService.createAssociation(association);
				logger.info("Successfully associated feedback ID: " + feedback.getFeedbackId() + " with question ID: "
						+ question.getQuestionId());
			} catch (ResourceUnableToCreate e) {
				logger.error("Failed to create association for feedback ID: " + feedback.getFeedbackId()
						+ " and question ID: " + question.getQuestionId(), e);
				throw e; // Re-throw the exception after logging
			}
		}

		logger.info("Completed associating feedback with questions for feedback ID: " + feedback.getFeedbackId());
	}

	/**
	 * Method to filter feedback based on employeeId, deviceId, rating, or
	 * merchantId. Before fetching feedback, checks if the employee, device, or
	 * merchant exists using their respective services.
	 *
	 * @param employeeId The ID of the employee.
	 * @param deviceId   The ID of the device.
	 * @param rating     The rating of the feedback.
	 * @param merchantId The ID of the merchant.
	 * @return List of feedbacks matching the criteria.
	 * @throws ResourceNotFoundException If the employee, device, or merchant does
	 *                                   not exist.
	 */
	public List<Feedback> getFeedbacksByFilters(Long employeeId, Long deviceId, Integer rating, Long merchantId)
			throws ResourceNotFoundException {

		logger.info("Fetching feedbacks by filters: employeeId=" + employeeId + ", deviceId=" + deviceId + ", rating="
				+ rating + ", merchantId=" + merchantId);

		if (merchantId != null) {
			logger.debug("Filtering feedbacks by merchant ID: " + merchantId);

			// Check if merchant exists using MerchantService
			if (!merchantService.existsById(merchantId)) {
				logger.error("Merchant with ID " + merchantId + " not found.");
				throw new ResourceNotFoundException("Merchant", "ID", String.valueOf(merchantId));
			}

			Optional<Merchant> merchantFromDb = merchantRepository.findById(merchantId);
			logger.debug("Merchant found: " + merchantFromDb.orElse(null));

			List<Feedback> feedbacks = feedbackRepository.findByFeedbackMerchant(merchantFromDb);
			logger.info("Found " + feedbacks.size() + " feedback(s) for merchant ID " + merchantId);
			return feedbacks;

		} else if (employeeId != null) {
			logger.debug("Filtering feedbacks by employee ID: " + employeeId);

			// Check if employee exists using EmployeeService
			if (!employeeService.existsById(employeeId)) {
				logger.error("Employee with ID " + employeeId + " not found.");
				throw new ResourceNotFoundException("Employee", "ID", String.valueOf(employeeId));
			}

			Optional<Employee> employeeFromDb = employeeRepository.findById(employeeId);
			logger.debug("Employee found: " + employeeFromDb.orElse(null));

			List<Feedback> feedbacks = feedbackRepository.findByFeedbackEmployee(employeeFromDb);
			logger.info("Found " + feedbacks.size() + " feedback(s) for employee ID " + employeeId);
			return feedbacks;

		} else if (deviceId != null) {
			logger.debug("Filtering feedbacks by device ID: " + deviceId);

			// Check if device exists using DeviceService
			if (!deviceService.existsById(deviceId)) {
				logger.error("Device with ID " + deviceId + " not found.");
				throw new ResourceNotFoundException("Device", "ID", String.valueOf(deviceId));
			}

			Optional<Device> deviceFromDb = deviceRepository.findById(deviceId);
			logger.debug("Device found: " + deviceFromDb.orElse(null));

			List<Feedback> feedbacks = feedbackRepository.findByFeedbackDevice(deviceFromDb);
			logger.info("Found " + feedbacks.size() + " feedback(s) for device ID " + deviceId);
			return feedbacks;

		} else if (rating != null) {
			logger.debug("Filtering feedbacks by rating: " + rating);

			List<Feedback> feedbacks = feedbackRepository.findByFeedbackRating(rating);
			logger.info("Found " + feedbacks.size() + " feedback(s) for rating " + rating);
			return feedbacks;

		} else {
			logger.debug("No specific filters provided, retrieving all feedbacks.");

			List<Feedback> feedbacks = feedbackRepository.findAll();
			logger.info("Found " + feedbacks.size() + " feedback(s).");
			return feedbacks;
		}
	}

	/**
	 * Counts the number of feedbacks for all employees.
	 *
	 * @return A list of EmployeeFeedbackCountDto containing employee ID, email, and
	 *         feedback count.
	 */
	public List<EmployeeFeedbackCountDto> countFeedbacksForAllEmployees() {
		logger.info("Fetching feedback counts for all employees...");

		// Retrieve feedback counts grouped by employee from the repository
		List<Object[]> results = feedbackRepository.countFeedbacksByEmployee();
		List<EmployeeFeedbackCountDto> feedbackCounts = new ArrayList<>();

		if (results == null || results.isEmpty()) {
			logger.warn("No feedback data found for any employees.");
		} else {
			logger.info("Successfully retrieved " + results.size() + " feedback count results from the repository.");
		}

		// Iterate over the results and map them to DTOs
		for (Object[] result : results) {
			try {
				Long employeeId = (Long) result[0]; // Extract the employee ID from the result
				Long feedbackCount = (Long) result[1]; // Extract the feedback count from the result

				// Log the extracted data for debugging purposes
				logger.debug("Employee ID: " + employeeId + ", Feedback Count: " + feedbackCount);

				// Optionally retrieve the employee details for email
				Employee employee = employeeRepository.findById(employeeId).orElse(null);
				String employeeEmail = (employee != null) ? employee.getEmployeeEmail() : "Unknown"; // Default to
																										// "Unknown" if
																										// not found

				// Log employee details, or lack thereof
				if (employee != null) {
					logger.debug("Employee found with email: " + employeeEmail);
				} else {
					logger.warn("Employee with ID " + employeeId + " not found. Defaulting email to 'Unknown'.");
				}

				// Create a new DTO and add it to the list
				feedbackCounts.add(new EmployeeFeedbackCountDto(employeeId, employeeEmail, feedbackCount.longValue()));
			} catch (Exception e) {
				// Log any issues that occur during result mapping
				logger.error("Error occurred while processing feedback count for employee: " + result[0], e);
			}
		}

		logger.info("Returning " + feedbackCounts.size() + " employee feedback counts.");
		return feedbackCounts; // Return the list of employee feedback counts
	}

	/**
	 * Retrieves the average rating of feedback grouped by device.
	 *
	 * @return A list of AverageRatingResponseDTO containing device ID and average
	 *         rating.
	 */
	public List<AverageRatingResponseDTO> getAverageRatingByDevice() {
		logger.info("Fetching average ratings by device...");

		// Retrieve average ratings grouped by device from the repository
		List<Object[]> results = feedbackRepository.avgRatingByDevice();
		List<AverageRatingResponseDTO> averageRatings = new ArrayList<>();

		if (results == null || results.isEmpty()) {
			logger.warn("No average ratings found for any devices.");
		} else {
			logger.info("Successfully retrieved " + results.size() + " results from the repository.");
		}

		// Iterate over the results and map them to DTOs
		for (Object[] result : results) {
			try {
				Long deviceId = (Long) result[0]; // Extract the device ID from the result
				Double averageRating = ((Number) result[1]).doubleValue(); // Cast to Number and then to Double

				// Log the extracted data
				logger.debug("Device ID: " + deviceId + ", Average Rating: " + averageRating);

				// Create a new DTO and add it to the list
				averageRatings.add(new AverageRatingResponseDTO(deviceId, averageRating));
			} catch (Exception e) {
				// Log any potential issues with mapping the results
				logger.error("Error occurred while mapping result to DTO: " + e.getMessage(), e);
			}
		}

		logger.info("Returning " + averageRatings.size() + " average ratings.");
		return averageRatings; // Return the list of average ratings
	}

	/**
	 * Counts the number of feedbacks for each device.
	 *
	 * @return A list of DeviceFeedbackCountDTO containing device ID and feedback
	 *         count.
	 */
	public List<DeviceFeedbackCountDTO> getFeedbackCountByDevice() {
		logger.info("Fetching feedback counts grouped by device.");

		List<DeviceFeedbackCountDTO> feedbackCounts = new ArrayList<>();

		try {
			// Retrieve feedback counts grouped by device from the repository
			List<Object[]> results = feedbackRepository.countFeedbacksByDevice();
			logger.info("Successfully retrieved feedback counts from repository. Results size: " + results.size());

			// Iterate over the results and map them to DTOs
			for (Object[] result : results) {
				Long deviceId = (Long) result[0]; // Extract the device ID from the result
				Long count = (Long) result[1]; // Extract the feedback count from the result

				// Log each extracted value
				logger.debug("Processing feedback count for deviceId: " + deviceId + ", count: " + count);

				// Create a new DTO and add it to the list
				feedbackCounts.add(new DeviceFeedbackCountDTO(deviceId, count));
			}

			logger.info("Feedback counts successfully mapped to DTOs. Total counts: " + feedbackCounts.size());

		} catch (Exception e) {
			logger.error("Error occurred while retrieving feedback counts by device.", e);
		}

		return feedbackCounts; // Return the list of feedback counts
	}

	public boolean checkFeedbackIntegrity(String feedbackText) {
		// TODO Auto-generated method stub
		return false;
	}
}
