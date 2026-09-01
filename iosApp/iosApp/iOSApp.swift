import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinStarter.shared.initializeDependencyGraph(onKoinStart: { _ in })
    }
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}