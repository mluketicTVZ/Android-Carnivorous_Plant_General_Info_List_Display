package hr.tvz.android.mvpluketic.contract

import hr.tvz.android.mvpluketic.model.Plant

interface PlantDetailContract {

    interface View {
        fun showPlant(plant: Plant)
        fun openWikiPage(url: String)
        fun openImageFullscreen(imageUrl: String, plantName: String)
        fun showShareConfirmation()
    }

    interface Presenter {
        fun onViewReady()
        fun onWikiClicked()
        fun onImageClicked()
        fun onShareConfirmed()
        fun onDestroy()
    }
}
