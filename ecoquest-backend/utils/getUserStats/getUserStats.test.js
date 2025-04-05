// getUserStats.test.js
import getUserStats from './getUserStats.js';
import connectWithConnector from '../../config/db/dbConnection.js';

jest.mock('./dbConnection'); // Mocking the connector

beforeEach(() => {
	// Reset all mocks to ensure each test starts clean
	jest.resetAllMocks();
});

describe('getUserStats', () => {
	let mockPool;

	beforeEach(() => {
		mockPool = {
			query: jest.fn(), // Mock the `query` method of the pool
		};

		connectWithConnector.mockResolvedValue(mockPool); // Mock connection returning the pool
	});

	afterEach(() => {
		jest.clearAllMocks(); // Clear mocks after each test
	});

	it('should return user stats when the query succeeds', async () => {
		const mockUserID = '1234';
		const mockRows = [{ categoria: 'easy', completed_missions_count: 10 }];

		// Mock the query result
		mockPool.query.mockResolvedValue([mockRows]);

		const result = await getUserStats(mockUserID);

		expect(connectWithConnector).toHaveBeenCalled(); // Ensure connector is called
		expect(mockPool.query).toHaveBeenCalledWith(
			'SELECT categoria, completed_missions_count FROM completed_missions_per_category WHERE id_usuario = ?',
			[mockUserID]
		);
		expect(result).toEqual([mockRows]); // Check the correct result is returned
	});

	it('should throw an error when the query fails', async () => {
		const mockUserID = '1234';
		const mockError = new Error('Database error');

		// Mock the query to throw an error
		mockPool.query.mockRejectedValue(mockError);

		await expect(getUserStats(mockUserID)).rejects.toThrow('Database error'); // Checking the error message

		expect(connectWithConnector).toHaveBeenCalled(); // Ensure connector is called
		expect(mockPool.query).toHaveBeenCalledWith(
			'SELECT categoria, completed_missions_count FROM completed_missions_per_category WHERE id_usuario = ?',
			[mockUserID]
		);
	});
});
