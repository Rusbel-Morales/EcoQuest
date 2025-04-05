//
//  generateReferralCode.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 11/4/24.
//

func generateReferralCode(userId: String) -> String {
    guard userId.count >= 5 else {
        // Ensure the userId has at least 5 characters
        return "INVALID"
    }
    
    let startIndex = userId.startIndex
    let endIndex = userId.endIndex
    
    // Extract the first 3 characters
    let firstPart = userId[startIndex..<userId.index(startIndex, offsetBy: 3)]
    
    // Extract the last 2 characters
    let lastPart = userId[userId.index(endIndex, offsetBy: -2)..<endIndex]
    
    // Combine both parts to create the referral code
    return String(firstPart + lastPart)
}
