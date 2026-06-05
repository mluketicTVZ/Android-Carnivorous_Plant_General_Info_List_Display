package hr.tvz.android.mvpluketic.presenter

import android.content.Context
import hr.tvz.android.mvpluketic.contract.PlantListContract
import hr.tvz.android.mvpluketic.repository.PlantRepository
import hr.tvz.android.mvpluketic.repository.PlantSeeder

class PlantListPresenter(
    view: PlantListContract.View,
    private val context: Context
) : PlantListContract.Presenter {

    private var view: PlantListContract.View? = view

    override fun loadPlants() {
        view?.showLoading()
        PlantSeeder.seedIfNeeded(context) {
            PlantRepository.getPlants(
                onSuccess = { plants ->
                    view?.hideLoading()
                    view?.showPlants(plants)
                },
                onError = { msg ->
                    view?.hideLoading()
                    view?.showError(msg)
                }
            )
        }
    }

    override fun onDestroy() {
        view = null
    }
}
