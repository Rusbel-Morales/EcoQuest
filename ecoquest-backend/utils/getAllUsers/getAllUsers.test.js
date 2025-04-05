// getAllUsers.test.js
import getAllUsers from './getAllUsers.js';
import connectWithConnector from '../dbConnection.js';

jest.mock('./dbConnection');

describe('getAllUsers', () => {
	it('should return rows from the database', async () => {
		// Arrange: Mock the connection and pool
		const mockExecute = jest.fn().mockResolvedValue([
			[
				{ id_usuario: 1, device_token: 'ABC123' },
				{ id_usuario: 2, device_token: 'XYZ789' },
			],
		]);
		const mockPool = { execute: mockExecute };

		connectWithConnector.mockResolvedValue(mockPool);

		// Act: Call the getAllUsers function
		const users = await getAllUsers();

		// Assert: Verify the output
		expect(users).toEqual([
			{ id_usuario: 1, device_token: 'ABC123' },
			{ id_usuario: 2, device_token: 'XYZ789' },
		]);
		expect(mockExecute).toHaveBeenCalledWith(
			'SELECT id_usuario, device_token FROM usuario WHERE device_token IS NOT NULL'
		);
	});
});
