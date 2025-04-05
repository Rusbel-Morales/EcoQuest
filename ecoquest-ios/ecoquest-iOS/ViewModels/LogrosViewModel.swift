//
//  LogrosViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 11/8/24.
//

import Foundation

struct AchievementsResponse: Codable {
    let achievements: [Achievement]
}

struct Achievement: Codable {
    let idAchievement: Int
    let isCompleted: Bool
    
    enum CodingKeys: String, CodingKey {
        case idAchievement = "id_achievement"
        case isCompleted
    }
    
    // Custom initializer to decode `isCompleted` as Bool
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        idAchievement = try container.decode(Int.self, forKey: .idAchievement)
        
        // Decode `isCompleted` as an Int, then map 1 to true and 0 to false
        let isCompletedInt = try container.decode(Int.self, forKey: .isCompleted)
        isCompleted = isCompletedInt == 1
    }
}

class LogrosViewModel: ObservableObject {
    @Published var achievements: [Achievement] = []
    @Published var errorMessage: String?
    @Published var isLoading: Bool = false
    
    func fetchAchievements(userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/stats/get-user-achievements?userId=\(userId)") else {
            return
        }
        
        print("Calling API")
        
        isLoading = true // Activar estado de carga
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            DispatchQueue.main.async {
                self?.isLoading = false // Detener el estado de carga
            }
            
            if let error = error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
                return
            }
            
            guard let data = data else {
                DispatchQueue.main.async {
                    self?.errorMessage = "No data received"
                }
                return
            }
            
            do {
                let achievementsResponse = try JSONDecoder().decode(AchievementsResponse.self, from: data)
                DispatchQueue.main.async {
                    self?.achievements = achievementsResponse.achievements
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
}
