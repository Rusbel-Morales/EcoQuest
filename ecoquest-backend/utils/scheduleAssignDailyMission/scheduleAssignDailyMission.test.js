// scheduleGetDailyMission.test.js
import cron from 'node-cron';
import getDailyMission from './getDailyMission.js';
import scheduleGetDailyMission from './scheduleAssignDailyMission.js';

jest.mock('node-cron'); // Mock node-cron to control the scheduling
jest.mock('./getDailyMission'); // Mock getDailyMission to control its behavior

describe('scheduleGetDailyMission', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	it('should schedule a job at 9:00 AM every day', () => {
		scheduleGetDailyMission();

		// Verify that node-cron schedule was called correctly
		expect(cron.schedule).toHaveBeenCalledTimes(1);
		expect(cron.schedule).toHaveBeenCalledWith(
			'0 9 * * *',
			expect.any(Function)
		);
	});

	it('should call getDailyMission for each user when the cron job runs', async () => {
		// Mock implementation for the cron schedule method
		let scheduledTask;
		cron.schedule.mockImplementation((time, task) => {
			scheduledTask = task; // Capture the scheduled task function
		});

		// Set up getDailyMission to return a valid mission object for each user
		getDailyMission.mockResolvedValue({
			id: 1,
			title: 'Camina o anda en bicicleta',
			category: 'Transporte',
			difficulty: 'Fácil',
			impact: '0.25 kg de CO2',
		});

		// Call the function to schedule the job
		scheduleGetDailyMission([1, 2, 3]);

		// Execute the captured task immediately to simulate the cron job running
		await scheduledTask();

		// Verify getDailyMission was called for each user
		expect(getDailyMission).toHaveBeenCalledTimes(3);
		expect(getDailyMission).toHaveBeenCalledWith(1);
		expect(getDailyMission).toHaveBeenCalledWith(2);
		expect(getDailyMission).toHaveBeenCalledWith(3);
	});

	it('should handle errors thrown by getDailyMission', async () => {
		// Mock getDailyMission to throw an error for testing error handling
		getDailyMission.mockRejectedValue(new Error('Failed to get mission'));

		// Mock implementation for the cron schedule method
		let scheduledTask;
		cron.schedule.mockImplementation((time, task) => {
			scheduledTask = task; // Capture the scheduled task function
		});

		// Spy on console.error to verify error handling
		const consoleErrorSpy = jest
			.spyOn(console, 'error')
			.mockImplementation(() => {});

		// Call the function to schedule the job
		scheduleGetDailyMission([1]);

		// Execute the captured task immediately to simulate the cron job running
		await scheduledTask();

		// Verify getDailyMission was called and error was logged
		expect(getDailyMission).toHaveBeenCalledTimes(1);
		expect(getDailyMission).toHaveBeenCalledWith(1);
		expect(consoleErrorSpy).toHaveBeenCalledWith(
			'Error assigning mission to user 1:',
			expect.any(Error)
		);

		consoleErrorSpy.mockRestore();
	});
});
