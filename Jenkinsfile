properties([
        parameters([
                choice(name: 'Invoke_Parameters', choices:"No\nYes", description: "Do you wish to do a dry run to grab parameters?" ),
                string(defaultValue: 'branchtest,null,null,null,null,null', description: '', name: 'branchtest'),
                string(defaultValue: 'borgias,null,null,null,null,null', description: '', name: 'borgias'),
                string(defaultValue: 'testing,null,null,null,null,null', description: '', name: 'testing')

        ])
])




def RunJob = {cloud ->
    try {
        stage(cloud) {

            build(job: deviceHealthCheckWIFI, propagate: false, wait: false, parameters: [string(name: 'mcmParams', value: "${cloud}")])

        }
    } catch (e) {
        echo e.toString()
    }
}


    stage("Parameterizing") {
        steps {
            script {
                if ("${params.Invoke_Parameters}" == "Yes") {
                    currentBuild.result = 'ABORTED'
                    error('DRY RUN COMPLETED. JOB PARAMETERIZED.')
                }

            }
        }
    }



node('generic-slaves') {
    timeout(time: 2, unit: 'HOURS'){
            parallel {

                RunJob('branchtest')
                //RunJob('borgias')
                //RunJob('testing')

            }
    }
}