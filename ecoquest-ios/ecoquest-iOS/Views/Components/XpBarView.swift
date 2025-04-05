//
//  XpBarView.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/17/24.
//

import SwiftUI

struct XPBar: View {
    @EnvironmentObject var authViewModel: AuthViewModel
    @StateObject private var viewModel: XpBarViewModel = XpBarViewModel()
    
    var body: some View {
        HStack(spacing: 8) {
            // Position Text
            Text("#\(viewModel.xpBarData?.miPosicion ?? -1 != -1 ? "\(viewModel.xpBarData?.miPosicion ?? 0)" : "N/A")")
                .font(.caption)
                .fontWeight(.bold)
                .foregroundColor(.gray)
            
            // XP Background bar
            ZStack(alignment: .leading) {
                RoundedRectangle(cornerRadius: 10)
                    .fill(Color(red: 162/255, green: 202/255, blue: 113/255)) // Green background
                    .frame(width: 160, height: 20)
                
                if viewModel.xpBarData?.miPosicion == 1 {
                    // Fully filled bar for first place
                    RoundedRectangle(cornerRadius: 10)
                        .fill(Color(red: 178/255, green: 235/255, blue: 242/255)) // Blue foreground
                        .frame(width: 160, height: 20)
                    
                    // Centered XP text for first place
                    Text("\(viewModel.xpBarData?.misPuntos ?? 0) EcoXP")
                        .font(.caption2)
                        .fontWeight(.semibold)
                        .foregroundColor(Color.black.opacity(0.8))
                        .frame(maxWidth: .infinity, alignment: .center)
                } else {
                    // Partially filled bar for other positions
                    RoundedRectangle(cornerRadius: 10)
                        .fill(Color(red: 178/255, green: 235/255, blue: 242/255)) // Blue foreground
                        .frame(width: getXpBarWidth(), height: 20)
                    
                    // Overlay XP text with next user points
                    Text("\(viewModel.xpBarData?.misPuntos ?? 0) / \(viewModel.xpBarData?.puntosSiguienteUsuario.map(String.init) ?? "N/A")")
                        .font(.caption2)
                        .fontWeight(.semibold)
                        .foregroundColor(Color.black.opacity(0.8))
                        .padding(.leading, 8)
                }
            }
        }
        .padding(.horizontal, 10)
        .frame(maxWidth: .infinity, alignment: .leading)
        .onAppear {
            viewModel.fetchXpBarData(userId: authViewModel.userId)
        }
    }
    
    // Helper to calculate XP bar width based on current and next user points
    private func getXpBarWidth() -> CGFloat {
        let currentPoints = CGFloat(viewModel.xpBarData?.misPuntos ?? 0)
        let nextUserPoints = CGFloat(viewModel.xpBarData?.puntosSiguienteUsuario ?? 1)
        let maxBarWidth: CGFloat = 160 // Adjusted width

        // Prevent exceeding the maxBarWidth and calculate the correct proportional width
        guard nextUserPoints > 0 else { return 0 }
        let width = (currentPoints / nextUserPoints) * maxBarWidth
        return min(width, maxBarWidth)
    }
}
