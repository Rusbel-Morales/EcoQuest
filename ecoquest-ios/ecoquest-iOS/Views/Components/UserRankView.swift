//
//  UserRankView.swift
//  ecoquest-iOS
//
//  Created by Administrador on 17/10/24.
//

import SwiftUI

struct UserRankView: View {
    
    var finalRank: Int = 4
    var finalPoints: String = "40"
    var userName: String = "Tú"
    var isUser: Bool = false
    
    var body: some View {
        HStack(spacing: 16) {
            RankBadge(rank: finalRank)
            
            Text(userName)
                .font(.system(size: 18, weight: .semibold))
                .foregroundColor(isUser ? .black : .primary)
            
            Spacer()
            
            PointsBadge(points: finalPoints)
        }
        .padding()
        .background(isUser ? Color(red: 162/255, green: 202/255, blue: 113/255) : Color.white)
        .cornerRadius(15)
        .shadow(radius: isUser ? 5 : 2)
        .padding(.horizontal)
    }
    
    struct RankBadge: View {
        var rank: Int
        
        var body: some View {
            let (fillColor, strokeColor, strokeWidth) = rankColors(for: rank)
            
            ZStack {
                Circle()
                    .fill(fillColor)
                    .frame(width: 40, height: 40)
                    .overlay(
                        Circle()
                            .stroke(strokeColor, lineWidth: strokeWidth)
                    )
                
                Text("#\(rank)")
                    .font(.system(size: 14, weight: .bold))
                    .foregroundColor(rank <= 3 ? .white : .primary)
            }
        }
        
        // Helper function to determine colors and stroke width based on rank
        func rankColors(for rank: Int) -> (Color, Color, CGFloat) {
            switch rank {
            case 1:
                return (Color(red: 255/255, green: 215/255, blue: 0/255),
                        Color(red: 212/255, green: 178/255, blue: 0/255), 3)
            case 2:
                return (Color(red: 192/255, green: 192/255, blue: 192/255),
                        Color(red: 154/255, green: 154/255, blue: 154/255), 3)
            case 3:
                return (Color(red: 205/255, green: 127/255, blue: 50/255),
                        Color(red: 183/255, green: 113/255, blue: 44/255), 3)
            default:
                return (Color(red: 224/255, green: 224/255, blue: 224/255),
                        Color.clear, 0)
            }
        }
    }
    
    struct PointsBadge: View {
        var points: String
        
        var body: some View {
            ZStack {
                RoundedRectangle(cornerRadius: 10)
                    .fill(Color(red: 224/255, green: 224/255, blue: 224/255).opacity(0.2))
                    .frame(width: 80, height: 40)
                
                Text(points)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundColor(.primary)
            }
        }
    }
}

#Preview {
    UserRankView()
}
