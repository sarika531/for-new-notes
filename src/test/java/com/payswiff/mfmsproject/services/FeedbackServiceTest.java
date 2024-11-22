package com.payswiff.mfmsproject.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.payswiff.mfmsproject.models.*;
import com.payswiff.mfmsproject.repositories.DeviceRepository;
import com.payswiff.mfmsproject.repositories.EmployeeRepository;
import com.payswiff.mfmsproject.repositories.FeedbackRepository;
import com.payswiff.mfmsproject.repositories.MerchantRepository;
import com.payswiff.mfmsproject.repositories.QuestionRepository;
import com.payswiff.mfmsproject.dtos.FeedbackQuestionAnswerAssignDto;
import com.payswiff.mfmsproject.reuquests.CreateFeedbackRequest;
import com.payswiff.mfmsproject.exceptions.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

class FeedbackServiceTest {

	@Mock
	private FeedbackRepository feedbackRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private DeviceRepository deviceRepository;

	@Mock
	private MerchantRepository merchantRepository;

	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private EmailService emailService;

	@Mock
	private MerchantDeviceAssociationService merchantDeviceAssociationService;

	@Mock
	private FeedbackQuestionsAssociationService feedbackQuestionsAssociationService;

	@InjectMocks
	private FeedbackService feedbackService;

	private CreateFeedbackRequest feedbackRequest;
	private List<FeedbackQuestionAnswerAssignDto> questionAnswers;
	private Employee employee;
	private Merchant merchant;
	private Device device;

	@BeforeEach
	void setUp() throws ResourceNotFoundException, ResourceUnableToCreate {
		// Initialize mock objects
		MockitoAnnotations.openMocks(this);

		// Sample feedback request data
		feedbackRequest = new CreateFeedbackRequest();
		feedbackRequest.setFeedbackEmployeeId(1L);
		feedbackRequest.setFeedbackMerchantId(1L);
		feedbackRequest.setFeedbackDeviceId(1L);
		feedbackRequest.setFeedbackRating(5.0);
		feedbackRequest.setFeedbackImage1("imageurl");
		feedbackRequest.setFeedback("Excellent service!");

		// Sample question answers
		questionAnswers = Arrays.asList(new FeedbackQuestionAnswerAssignDto(1L, "Yes"),
				new FeedbackQuestionAnswerAssignDto(2L, "No"));

		// Sample employee, merchant, and device data
		employee = new Employee(1L, UUID.randomUUID().toString(), "payswiffID", "John Doe", "bapanapalligopi1@gmail.com",
				"password", "1234567890", "dev", EmployeeType.employee, null, null, null);
		merchant = new Merchant(1L, UUID.randomUUID().toString(), "Merchant Inc.", "merchant@example.com", "1234567890",
				"Merchant Business", null, null, null);
		device = new Device(1L, UUID.randomUUID().toString(), "Device Model", "Device Manufacturer", null, null);

		// Mock repository methods
		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
		when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
		when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
		when(merchantDeviceAssociationService.isDeviceAssociatedWithMerchant(1L, 1L)).thenReturn(true);
	}

	@AfterEach
	void tearDown() {
		// Reset mocks after each test
		reset(feedbackRepository, employeeRepository, deviceRepository, merchantRepository, emailService,
				merchantDeviceAssociationService);
	}

	/**
	 * Test case for creating feedback. Verifies that when all entities exist and
	 * are valid, feedback is successfully created.
	 */
//	@Test
//	void testCreateFeedback_Success() throws Exception {
//	    // Create mock feedback with an initialized Employee
//	    Feedback feedback = new Feedback();
//	    Employee employee = new Employee(1L, UUID.randomUUID().toString(), "payswiffID", "John Doe",
//	            "bapanapalligopi1@gmail.com", "password", "1234567890", "dev", EmployeeType.employee, null, null, null);
//	    Merchant merchant = new Merchant(1L, UUID.randomUUID().toString(), "Merchant Inc.", "merchant@example.com",
//	            "1234567890", "Merchant Business", null, null, null);
//	    Device device = new Device(1L, UUID.randomUUID().toString(), "Device Model", "Device Manufacturer", null, null);
//
//	    feedback.setFeedbackEmployee(employee);
//	    feedback.setFeedbackMerchant(merchant);
//	    feedback.setFeedbackDevice(device);
//
//	    // Create a mock FeedbackQuestionsAssociation
//	    FeedbackQuestionsAssociation mockAssociation = new FeedbackQuestionsAssociation();
//	    
//	    // Mock the repository save method to return the mock feedback
//	    when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
//
//	    // Mock the sendEmail method to return true (simulate successful email sending)
//	    when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
//
//	    // Mock the createAssociation method to return a valid mock association
//	    when(feedbackQuestionsAssociationService.createAssociation(any(FeedbackQuestionsAssociation.class)))
//	        .thenReturn(mockAssociation);
//
//	    // Call the method under test
//	    boolean result = feedbackService.createFeedback(feedbackRequest, questionAnswers);
//
//	    // Verify the feedback repository's save method was called once
//	    verify(feedbackRepository, times(1)).save(any(Feedback.class));
//
//	    // Verify the feedback questions association creation
//	    verify(feedbackQuestionsAssociationService, times(1))
//	            .createAssociation(any(FeedbackQuestionsAssociation.class));  // Ensure it's invoked once
//
//	    // Verify email sending logic
//	    verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
//
//	    // Assert that the result is true
//	    assertTrue(result);
//	}

	/**
	 * Test case for creating feedback when the employee does not exist. Verifies
	 * that the appropriate exception is thrown.
	 */
	@Test
	void testCreateFeedback_EmployeeNotFound() {
		// Simulate that employee doesn't exist in the repository
		when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

		// Call the method under test and assert that the exception is thrown
		assertThrows(ResourceNotFoundException.class, () -> {
			feedbackService.createFeedback(feedbackRequest, questionAnswers);
		});
	}

	/**
	 * Test case for creating feedback when the merchant does not exist. Verifies
	 * that the appropriate exception is thrown.
	 */
	@Test
	void testCreateFeedback_MerchantNotFound() {
		// Simulate that merchant doesn't exist in the repository
		when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

		// Call the method under test and assert that the exception is thrown
		assertThrows(ResourceNotFoundException.class, () -> {
			feedbackService.createFeedback(feedbackRequest, questionAnswers);
		});
	}

	/**
	 * Test case for creating feedback when the device does not exist. Verifies that
	 * the appropriate exception is thrown.
	 */
	@Test
	void testCreateFeedback_DeviceNotFound() {
		// Simulate that device doesn't exist in the repository
		when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

		// Call the method under test and assert that the exception is thrown
		assertThrows(ResourceNotFoundException.class, () -> {
			feedbackService.createFeedback(feedbackRequest, questionAnswers);
		});
	}

	/**
	 * Test case for creating feedback when the merchant-device association is
	 * missing. Verifies that the appropriate exception is thrown.
	 * 
	 * @throws ResourceUnableToCreate
	 * @throws ResourceNotFoundException
	 */
	@Test
	void testCreateFeedback_MerchantDeviceNotAssigned() throws ResourceNotFoundException, ResourceUnableToCreate {
		// Simulate that the device is not associated with the merchant
		when(merchantDeviceAssociationService.isDeviceAssociatedWithMerchant(1L, 1L)).thenReturn(false);

		// Call the method under test and assert that the exception is thrown
		assertThrows(MerchantDeviceNotAssignedException.class, () -> {
			feedbackService.createFeedback(feedbackRequest, questionAnswers);
		});
	}

	/**
	 * Test case for creating feedback when email sending fails. Verifies that the
	 * failure email is sent and the exception is thrown.
	 */
//	@Test
//	void testCreateFeedback_EmailSendFailure() throws Exception {
//		// Simulate a failure in sending email
//		doThrow(new UnableSentEmail("bapanapalligopi1@gmail.com")).when(emailService).sendEmail(anyString(), anyString(),
//				anyString());
//
//		// Call the method under test and assert that the exception is thrown
//		assertThrows(UnableSentEmail.class, () -> {
//			feedbackService.createFeedback(feedbackRequest, questionAnswers);
//		});
//	}

}
