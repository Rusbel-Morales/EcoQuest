//
//  dayOfWeek.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 11/5/24.
//
import Foundation

func dayOfWeek(dateString: String) -> String? {
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "dd-MM-yyyy"
    dateFormatter.locale = Locale(identifier: "es_ES") // Set locale to Spanish (Spain)
    
    guard let date = dateFormatter.date(from: dateString) else {
        return nil // Return nil if date is invalid
    }
    
    // Get the day of the week abbreviation
    dateFormatter.dateFormat = "EEE"
    let dayOfWeek = dateFormatter.string(from: date)
    
    return dayOfWeek.capitalized // Capitalize first letter (e.g., "Lun", "Mar")
}
