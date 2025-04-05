//
//  EstadisticasSectionViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/17/24.
//

import Foundation

struct UserStats: Codable {
    let userId: String
    let rachaActual: Int
    let rachaMaxima: Int
    let totalPuntos: String
    let totalMisionesCompletadas: Int
}

class EstadisticasSectionViewModel: ObservableObject {
    @Published var errorMessage: String?
    @Published var userStats: UserStats?
    
    func fetchStatsData(userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/stats/get-user-stats?userId=\(userId)") else {
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
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
                let userStatsResponse = try JSONDecoder().decode(UserStats.self, from: data)
                DispatchQueue.main.async {
                    self?.userStats = userStatsResponse
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
}
