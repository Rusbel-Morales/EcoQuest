//
//  PointsByDateViewModel.swift
//  ecoquest-iOS
//
//  Created by Administrador on 01/11/24.
//

import Foundation

// Estructura que representa los puntos obtenidos en un día específico
struct DailyPoints: Codable {
    let dia: String
    let puntos_totales: Int
}

class PointsByDateViewModel: ObservableObject {
    @Published var dailyPoints: [DailyPoints] = []
    @Published var errorMessage: String?
    @Published var isLoading: Bool = false
   
    // Inicializador que permite usar datos de prueba para el preview
    init(testData: [DailyPoints]? = nil) {
        if let testData = testData {
            self.dailyPoints = testData
        }
    }
   
    func fetchPointsByDate(userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/stats/get-points-day?userId=\(userId)") else {
            print("URL inválida")
            return
        }
        
        isLoading = true // Activar estado de carga
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
       
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            DispatchQueue.main.async {
                self?.isLoading = false // Desactivar estado de carga
            }
            
            if let error = error {
                DispatchQueue.main.async {
                    self?.errorMessage = "Error en la solicitud: \(error.localizedDescription)"
                }
                print("Error en la solicitud: \(error.localizedDescription)")
                return
            }
           
            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                DispatchQueue.main.async {
                    self?.errorMessage = "Respuesta inválida del servidor"
                }
                print("Respuesta inválida del servidor")
                return
            }
           
            guard let data = data else {
                DispatchQueue.main.async {
                    self?.errorMessage = "No se recibieron datos del servidor"
                }
                print("No se recibieron datos del servidor")
                return
            }
           
            do {
                let dailyPointsResponse = try JSONDecoder().decode([DailyPoints].self, from: data)
                DispatchQueue.main.async {
                    self?.dailyPoints = dailyPointsResponse
                    print("Datos recibidos y decodificados: \(self?.dailyPoints ?? [])")
                }
            } catch let decodingError {
                DispatchQueue.main.async {
                    self?.errorMessage = "Error al decodificar los datos: \(decodingError.localizedDescription)"
                }
                print("Error al decodificar los datos: \(decodingError.localizedDescription)")
            }
        }.resume()
    }
   
    func numberOfWeeks() -> Int {
        let numberOfDays = dailyPoints.count
        return (numberOfDays / 7) + (numberOfDays % 7 == 0 ? 0 : 1)
    }
}




