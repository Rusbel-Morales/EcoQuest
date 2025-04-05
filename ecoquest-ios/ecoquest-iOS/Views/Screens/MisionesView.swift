//
//  Misiones.swift
//  ecoquest-iOS
//
//  Created by Administrador on 24/09/24.
//

import SwiftUI

struct Misiones: View {
    @Binding var path: NavigationPath
    @EnvironmentObject var authViewModel: AuthViewModel
    @StateObject private var viewModel = MisionesViewModel()
    @State private var isShareModalPresented = false
    @State private var isShareSheetPresented = false
    @State private var isMissionDescriptionModalPresented = false
    @State private var selectedMission: Mission?
    @State private var completedMission: Mission?
    
    private func fetchMissionIfNeeded() {
        if !authViewModel.userId.isEmpty {
            viewModel.fetchDailyMission(userId: authViewModel.userId)
        }
    }
    
    var body: some View {
        ZStack {
            VStack(spacing: 20) {
                TopBar(isShareModalPresented: $isShareModalPresented)
                    .padding(.top, 10)
                
                Image("logo-ecoquest")
                    .resizable()
                    .scaledToFit()
                    .frame(height: 80)
                    .cornerRadius(12)
                    .padding(.vertical, 10)
                
                DailyMissionView(
                    path: $path,
                    viewModel: viewModel,
                    signInViewModel: authViewModel,
                    selectedMission: $selectedMission,
                    completedMission: $completedMission,
                    isShareSheetPresented: $isShareSheetPresented
                )
                .padding(.vertical, 8)
                
                OptionalMissionsView(
                    path: $path,
                    selectedMission: $selectedMission,
                    completedMission: $completedMission,
                    isShareSheetPresented: $isShareSheetPresented
                )
                .padding(.vertical, 8)
                
                Spacer()
            }
            .padding(.horizontal, 16)
            .background(Color.white)
            
            if selectedMission != nil {
                Color.black.opacity(0.5)
                    .edgesIgnoringSafeArea(.all)
                    .onTapGesture {
                        isMissionDescriptionModalPresented = false
                        selectedMission = nil
                    }
                
                MissionDescriptionModalView(
                    isPresented: $isMissionDescriptionModalPresented,
                    mission: $selectedMission
                )
                .frame(maxWidth: 320, maxHeight: 380)
                .background(Color.white)
                .cornerRadius(20)
                .shadow(radius: 15)
                .padding()
            }
        }
        .onAppear {
            fetchMissionIfNeeded()
        }
        .overlay(
            Group {
                if self.isShareModalPresented {
                    Color.black.opacity(0.5)
                        .edgesIgnoringSafeArea(.all)
                    InviteFriendsModalView(isPresented: $isShareModalPresented)
                }
            }
        )
    }
}

#Preview {
    Misiones(path: .constant(NavigationPath()))
}
