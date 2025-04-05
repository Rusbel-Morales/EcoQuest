import SwiftUI

struct TrofeosView: View {
    @EnvironmentObject var authViewModel: AuthViewModel
    @StateObject private var viewModel = TrofeosViewModel()
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Trofeos")
                .font(.title2)
                .fontWeight(.bold)
                .padding(.bottom, 8)
            
            if viewModel.isLoading {
                ProgressView()
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 8)
            } else {
                HStack(alignment: .center, spacing: 15) {
                    RankBadge(type: "Oro", count: viewModel.trophyCount?.totalOro ?? 0, color: .yellow)
                    RankBadge(type: "Plata", count: viewModel.trophyCount?.totalPlata ?? 0, color: .gray)
                    RankBadge(type: "Bronce", count: viewModel.trophyCount?.totalBronce ?? 0, color: Color(red: 205/255, green: 127/255, blue: 50/255))
                }
                .padding(.vertical, 10)
                .frame(maxWidth: .infinity)
            }
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 8)
        .cornerRadius(8)
        .onAppear {
            viewModel.fetchTrophyCount(userId: authViewModel.userId)
        }
    }
}

struct RankBadge: View {
    var type: String
    var count: Int
    var color: Color
    
    var body: some View {
        VStack(spacing: 4) {
            Image(systemName: "trophy.fill")
                .resizable()
                .scaledToFit()
                .frame(width: 40, height: 40)
                .foregroundColor(color)
            Text("\(count) \(type)")
                .font(.system(size: 13, weight: .semibold))
                .foregroundColor(.primary)
        }
        .frame(width: 70, height: 90)
        .background(Color.white.opacity(0.9))
        .cornerRadius(6)
        .shadow(radius: 2)
    }
}
