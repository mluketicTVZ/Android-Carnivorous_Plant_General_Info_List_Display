package hr.tvz.android.mvpluketic.presenter

import android.content.Context
import android.content.Intent
import hr.tvz.android.mvpluketic.ShareBroadcastReceiver
import hr.tvz.android.mvpluketic.contract.PlantDetailContract
import hr.tvz.android.mvpluketic.model.Plant

class PlantDetailPresenter(
    view: PlantDetailContract.View,
    private val plant: Plant,
    private val context: Context
) : PlantDetailContract.Presenter {

    private var view: PlantDetailContract.View? = view

    override fun onViewReady() {
        view?.showPlant(plant)
    }

    override fun onWikiClicked() {
        view?.openWikiPage(plant.wikiUrl)
    }

    override fun onImageClicked() {
        view?.openImageFullscreen(plant.imageUrl, plant.name)
    }

    override fun onShareConfirmed() {
        val intent = Intent(ShareBroadcastReceiver.ACTION_PLANT_SHARED).apply {
            setPackage(context.packageName)
            putExtra("plant_name", plant.name)
        }
        context.sendBroadcast(intent)
        view?.showShareConfirmation()
    }

    override fun onDestroy() {
        view = null
    }
}
