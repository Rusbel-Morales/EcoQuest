// getDailyMission.test.js

// Use `jest.unstable_mockModule` to mock the ESM modules
jest.unstable_mockModule('../../AI/aiService.js', () => ({
	sendMessage: jest.fn(),
}));
jest.unstable_mockModule('../getUserStats/getUserStats.js', () => ({
	getUserStats: jest.fn(),
}));

// Asynchronously import the modules after mocking
let assignNewDailyMission;
let sendMessage;
let getUserStats;

beforeEach(async () => {
	// Import the modules asynchronously
	assignNewDailyMission = (await import('./getDailyMission.js')).default;
	sendMessage = (await import('../../services/aiService.js')).sendMessage;
	getUserStats = (await import('../getUserStats/getUserStats.js')).getUserStats;

	// Reset all mocks before each test
	jest.resetAllMocks();
});

describe('assignNewDailyMission', () => {
	it('should return the mission ID for a valid selected mission based on user stats', async () => {
		// Mock the response from getUserStats to return user stats
		const mockUserStats = { completedMissions: 5, points: 20 };
		getUserStats.mockResolvedValue(mockUserStats);

		// Mock the response from sendMessage to return a specific mission ID
		sendMessage.mockResolvedValue(3);

		// Call the function
		const result = await assignNewDailyMission(1); // Pass a valid userId

		// Expectations
		expect(getUserStats).toHaveBeenCalledWith(1);
		expect(sendMessage).toHaveBeenCalledTimes(1);
		expect(result).toEqual(3); // Expect the selected mission ID
	});

	it('should return undefined if the selected mission ID does not exist', async () => {
		// Mock the response from getUserStats to return user stats
		const mockUserStats = { completedMissions: 5, points: 20 };
		getUserStats.mockResolvedValue(mockUserStats);

		// Mock the response from sendMessage to return a non-existent mission ID
		sendMessage.mockResolvedValue(999);

		// Call the function
		const result = await assignNewDailyMission(1);

		// Expectations
		expect(getUserStats).toHaveBeenCalledWith(1);
		expect(sendMessage).toHaveBeenCalledTimes(1);
		expect(result).toBeUndefined(); // Mission with ID 999 doesn't exist in the `missions` array
	});

	it('should return a mission ID for a user with no stats', async () => {
		// Mock the response from getUserStats to return no data (user with no stats)
		getUserStats.mockResolvedValue(null);

		// Mock the response from sendMessage to return a specific mission ID
		sendMessage.mockResolvedValue(2);

		// Call the function
		const result = await assignNewDailyMission(2); // Pass a userId with no stats

		// Expectations
		expect(getUserStats).toHaveBeenCalledWith(2);
		expect(sendMessage).toHaveBeenCalledTimes(1);
		expect(result).toEqual(2); // Mission ID returned for a user with no stats
	});

	it('should throw an error if sendMessage fails', async () => {
		// Mock the response from getUserStats to return valid user stats
		const mockUserStats = { completedMissions: 5, points: 20 };
		getUserStats.mockResolvedValue(mockUserStats);

		// Mock sendMessage to throw an error
		sendMessage.mockRejectedValue(new Error('AI service failed'));

		// Call the function and expect an error to be thrown
		await expect(assignNewDailyMission(1)).rejects.toThrow('AI service failed');
	});

	it('should throw an error if getUserStats fails', async () => {
		// Mock getUserStats to throw an error
		getUserStats.mockRejectedValue(new Error('Failed to get user stats'));

		// Call the function and expect an error to be thrown
		await expect(assignNewDailyMission(1)).rejects.toThrow(
			'Failed to get user stats'
		);
	});
});
