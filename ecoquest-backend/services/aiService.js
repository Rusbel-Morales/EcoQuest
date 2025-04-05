import { VertexAI } from '@google-cloud/vertexai';

// Initialize Vertex with your Cloud project and location
const vertex_ai = new VertexAI({
	project: 'ecoquest-436100',
	location: 'us-central1',
});
const model = 'gemini-1.5-flash-002';

// Instantiate the models
const generativeModel = vertex_ai.preview.getGenerativeModel({
	model: model,
	generationConfig: {
		maxOutputTokens: 8192,
		temperature: 1,
		topP: 0.95,
	},
	safetySettings: [
		{
			category: 'HARM_CATEGORY_HATE_SPEECH',
			threshold: 'OFF',
		},
		{
			category: 'HARM_CATEGORY_DANGEROUS_CONTENT',
			threshold: 'OFF',
		},
		{
			category: 'HARM_CATEGORY_SEXUALLY_EXPLICIT',
			threshold: 'OFF',
		},
		{
			category: 'HARM_CATEGORY_HARASSMENT',
			threshold: 'OFF',
		},
	],
});

const chat = generativeModel.startChat({});

async function sendMessage(message) {
	const streamResult = await chat.sendMessageStream(message);
	const content = (await streamResult.response).candidates[0].content;

	// Extract the text value from the response
	const value = content.parts[0].text;

	// Convert the value to a number
	const result = parseInt(value, 10);

	return result;
}

export default sendMessage;
