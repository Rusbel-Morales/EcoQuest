// Function to generate a unique user code based on the user ID
function generateUserCode(userId) {
	return (userId.slice(0, 3) + userId.slice(-2)).toUpperCase();
}

export default generateUserCode;