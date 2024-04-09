
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            val database = getDatabase(applicationContext)
            val repository = AsteroidsRepository(database)
            val currentDate = Calendar.getInstance().time
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val startDate = dateFormat.format(currentDate)
            val formattedEndDate = dateFormat.format(endDate.time)
            repository.refreshAsteroids(startDate, formattedEndDate)
            Result.success()
        } catch (a: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

}