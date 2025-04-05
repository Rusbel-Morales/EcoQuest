//
//  LeaderboardView.swift
//  ecoquest-iOS
//
//  Created by Administrador on 16/10/24.
//

import SwiftUI

struct LeaderboardView: View {
    @EnvironmentObject var authViewModel: AuthViewModel
    @Binding var path: NavigationPath
    @StateObject private var viewModel = LeaderboardViewModel()
    @State private var finalRank: Int = 0
    @State private var finalPoints: String = "0"
    
    private func updateCurrentUserRankAndPoints() {
        if let currentUserIndex = viewModel.leaderboard.firstIndex(where: { $0.idUsuario == authViewModel.userId }) {
            finalRank = currentUserIndex + 1
            finalPoints = viewModel.leaderboard[currentUserIndex].totalPuntos
        }
    }
    
    var body: some View {
        VStack {
            // Título
            VStack {
                Text("Clasificación")
                    .font(.title)
                    .bold()
            }
            .padding()
            .frame(maxWidth: .infinity, maxHeight: 120, alignment: .bottom)
            .background(Color(red: 181/255, green: 222/255, blue: 135/255))
            .cornerRadius(10)
           
            Spacer().frame(height: 20)
            
            if viewModel.isLoading {
                ProgressView()
                    .frame(maxWidth: .infinity)
                    .padding()
            } else if viewModel.leaderboard.isEmpty {
                Text("No hay datos de leaderboard.")
            } else {
                // Lista de posiciones
                ScrollView {
                    LazyVStack(alignment: .leading, spacing: 15) {
                        ForEach(Array(viewModel.leaderboard.enumerated()), id: \.element.id) { index, user in
                            let isCurrentUser = user.idUsuario == authViewModel.userId

                            UserRankView(
                                finalRank: index + 1,
                                finalPoints: user.totalPuntos,
                                userName: user.nombre,
                                isUser: isCurrentUser
                            )
                        }
                    }
                    .padding(.top, 10)
                }
                .frame(maxHeight: 480)
                .refreshable {
                    await refreshLeaderboard()
                }
            }
           
            Spacer()
                .frame(height: 40)
            
            //Ranking del usuario repetido
            VStack {
                //Ranking del usuario
                UserRankView(finalRank: finalRank, finalPoints: finalPoints)
            }
        }
        .frame(maxHeight: .infinity, alignment: .top)
        .background(Color(red: 245/255, green: 245/255, blue: 245/255))
        .edgesIgnoringSafeArea(.all)
        .onAppear {
            viewModel.fetchLeaderboard()
        }
        .onChange(of: viewModel.leaderboard) {
            updateCurrentUserRankAndPoints()
        }
    }
    
    // Asynchronous function to handle pull-to-refresh action
    private func refreshLeaderboard() async {
        viewModel.fetchLeaderboard()
        updateCurrentUserRankAndPoints()
    }
}

#Preview {
    LeaderboardView(path: .constant(NavigationPath()))
}

