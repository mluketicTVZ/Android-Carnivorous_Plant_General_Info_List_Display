package hr.tvz.android.mvpluketic.contract

import hr.tvz.android.mvpluketic.model.Plant

interface PlantListContract {

    interface View {
        fun showPlants(plants: List<Plant>)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun loadPlants()
        fun onDestroy()
    }
}
