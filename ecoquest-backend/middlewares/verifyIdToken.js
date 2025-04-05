import fetch from 'node-fetch';

async function verifyIdToken(idToken) {
	try {
		const response = await fetch(
			`https://oauth2.googleapis.com/tokeninfo?id_token=${idToken}`
		);
		const data = await response.json();

		// Destructure email and name
		const { email, name } = data;

		return { email, name };
	} catch (error) {
		console.error('Error verifying token:', error);
		return null;
	}
}

export default verifyIdToken;
