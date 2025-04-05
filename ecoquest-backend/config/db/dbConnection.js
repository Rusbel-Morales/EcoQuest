import mysql from 'mysql2/promise';
import { Connector } from '@google-cloud/cloud-sql-connector';
import dotenv from 'dotenv';

dotenv.config();

const connectWithConnector = async () => {
	const connector = new Connector();
	const clientOpts = await connector.getOptions({
		instanceConnectionName: process.env.INSTANCE_CONNECTION_NAME,
		authType: 'IAM',
	});
	const dbConfig = {
		...clientOpts,
		user: process.env.DB_USER,
		database: process.env.DB_NAME,
	};
	return mysql.createPool(dbConfig);
};

export default connectWithConnector;
