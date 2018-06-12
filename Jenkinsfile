
properties([
        parameters([
                choice(name: 'Invoke_Parameters', choices:"Yes\nNo", description: "Do you wish to do a dry run to grab parameters?" ),
                string(defaultValue: 'branchtest,null,null,null,null,null', description: '', name: 'branchtest'),
                string(defaultValue: 'borgias,e2e-auto@perfectomobile.com,Aa123456,pmR&Dlab,null,Rndlab123', description: '', name: 'borgias'),
                string(defaultValue: 'testing,null,null,null,null,null', description: '', name: 'testing'),
                text(defaultValue: '', description: '', name: 'deviceBlackList'),
                text(defaultValue: 'BOS', description: '', name: 'siteWhitelist'),
                string(defaultValue: '50', description: '', name: 'maxParallelDevicesToRunOnCloud')

        ])
])




def RunJob = {cloud ->
    try {
        stage(cloud) {

            def job = build(job: 'deviceHealthCheckWIFI', propagate: false, wait: false,
                    parameters:
                            [text(name: 'mcmParams', value: params."$cloud"),
                             text(name: 'deviceBlackList', value: "${params.deviceBlackList}"),
                             text(name: 'siteWhitelist', value: "${params.siteWhitelist}"),
                             string(name: 'maxParallelDevicesToRunOnCloud', value: "${params.maxParallelDevicesToRunOnCloud}")
                            ])

        }
    } catch (e) {
        currentBuild.result = 'FAILURE'
        echo e.toString()

    }
}


    stage("Parameterizing") {

            script {
                if ("${params.Invoke_Parameters}" == "Yes") {
                    currentBuild.result = 'ABORTED'
                    error('DRY RUN COMPLETED. JOB PARAMETERIZED.')
                }

            }

    }



node('generic-slaves') {
    timeout(time: 2, unit: 'HOURS') {

                RunJob('branchtest')
                RunJob('borgias')
                RunJob('testing')

    }


}