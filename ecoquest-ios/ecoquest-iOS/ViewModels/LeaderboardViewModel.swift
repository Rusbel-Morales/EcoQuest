//
//  LeaderboardViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/16/24.
//

import Foundation

// Estructura que representa cada entrada del leaderboard
struct LeaderboardEntry: Codable, Identifiable, Equatable {
    var idUsuario: String
    var nombre: String
    var totalPuntos: String
    
    var id: String { idUsuario }
    
    // Mapea las claves JSON con las propiedades de la estructura en Swift
    enum CodingKeys: String, CodingKey {
        case idUsuario = "id_usuario"
        case nombre
        case totalPuntos = "total_puntos"
    }
}

// Estructura que representa la respuesta completa de la API
struct LeaderboardResponse: Codable {
    let leaderboard: [LeaderboardEntry]
}

class LeaderboardViewModel: ObservableObject {
    @Published var leaderboard: [LeaderboardEntry] = []
    @Published var errorMessage: String?
    @Published var isLoading: Bool = false
    
    func fetchLeaderboard() {
        guard let url = URL(string: "\(Config.serverURL)/stats/get-leaderboard") else {
            return
        }
        
        isLoading = true // Activar estado de carga
        
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            DispatchQueue.main.async {
                self?.isLoading = false  // Desactivar estado de carga
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
                let leaderboardResponse = try JSONDecoder().decode(LeaderboardResponse.self, from: data)
                DispatchQueue.main.async {
                    self?.leaderboard = leaderboardResponse.leaderboard
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
}
