import express from 'express';
import { verifyUser } from '../controllers/authController.js';

const router = express.Router();

// Route to verify a user's ID token and optionally create the user
router.post('/auth-user', verifyUser);

export default router;