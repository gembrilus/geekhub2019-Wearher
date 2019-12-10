package iv.nakonechnyi.worldweather.services.jobscheduler

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import iv.nakonechnyi.worldweather.services.weatherservice.WeatherService
import java.util.concurrent.TimeUnit

class SchedulerJobService : JobService(){

    override fun onStartJob(params: JobParameters?): Boolean {
        startService(Intent(this, WeatherService::class.java))
        return true
    }

    override fun onStopJob(params: JobParameters?) = false

    companion object{

        private const val WEATHER_JOB_ID = 1

        fun scheduleJob(context: Context): Int {
            val scheduler = context.getSystemService(JobScheduler::class.java)!!
            if (scheduler.allPendingJobs
                    .any { it.id == WEATHER_JOB_ID })
                return JobScheduler.RESULT_SUCCESS

            val jobService = ComponentName(context, SchedulerJobService::class.java)

            val jobBuilder = JobInfo.Builder(WEATHER_JOB_ID, jobService)
                .setPeriodic(TimeUnit.HOURS.toMillis(2L))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)

            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            return jobScheduler.schedule(jobBuilder.build())
        }

        fun cancelJob(context: Context) {
            val jobService = context.getSystemService(JobScheduler::class.java)
            jobService?.cancel(WEATHER_JOB_ID)
        }

    }

}