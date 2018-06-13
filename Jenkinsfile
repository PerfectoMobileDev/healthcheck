

import org.yaml.snakeyaml.Yaml




properties([
        parameters([
                choice(name: 'Invoke_Parameters', choices:"Yes\nNo", description: "Do you wish to do a dry run to grab parameters?" ),
                /*string(defaultValue: 'branchtest,null,null,null,null,null', description: '', name: 'branchtest'),
                string(defaultValue: 'borgias,e2e-auto@perfectomobile.com,Aa123456,pmR&Dlab,null,Rndlab123', description: '', name: 'borgias'),
                string(defaultValue: 'testing,null,null,null,null,null', description: '', name: 'testing'),*/
                text(defaultValue: '', description: '', name: 'deviceBlackList'),
                text(defaultValue: 'BOS', description: '', name: 'siteWhitelist'),
                string(defaultValue: '50', description: '', name: 'maxParallelDevicesToRunOnCloud')

        ])
])




def RunJob = {cloudParams ->
    try {
        stage(cloudParams.split(',')[0]) {

            def job = build(job: 'deviceHealthCheckWIFI', propagate: false, wait: false,
                    parameters:
                            [text(name: 'mcmParams', value: cloudParams),
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

        /*RunJob('branchtest,null,null,null,null,null,B7284D9E402EECA516D2504EB95FBF75911CD71D')
        RunJob('borgias,e2e-auto@perfectomobile.com,Aa123456,pmR&Dlab,null,Rndlab123,CEBA9DC67218B41C163F3AE101A21E5F53F162BE')*/

        stage("YAML") {

            script {
                Yaml parser = new Yaml()
                def obj = parser.load(("radius_4.yaml" as File).text)
                List clouds = obj.BOS

                clouds.each{echo it.toString()}

                }

            }

        }



/*
        RunJob('census,null,null,null,null,null')
        RunJob('qapm,null,null,null,null,null')
        RunJob('devcloud,null,null,null,null,null')
        RunJob('sap,null,null,null,null,null')
        RunJob('mastercard,null,null,null,null,null')
        RunJob('citibanamex,null,null,null,null,null')
        RunJob('citimobilemonitoring,null,null,null,null,null')
        RunJob('ally,null,null,null,null,null')
        RunJob('pepper,null,null,null,null,null')
        RunJob('ultramobile,null,null,null,null,null')
        RunJob('pnc,null,null,null,null,null')
        RunJob('nielsen,null,null,null,null,null')
        RunJob('msces,null,null,null,null,null')
        RunJob('onboarding,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('northerntrust,null,null,null,null,null')
        RunJob('dsg,null,null,null,null,null')
        RunJob('hcsc,null,null,null,null,null')
        RunJob('aeo,null,null,null,null,null')
        RunJob('test-directv,null,null,null,null,null')
        RunJob('fordtest,null,null,null,null,null')
        RunJob('ets-db,null,null,null,null,null')
        RunJob('frb,null,null,null,null,null')
        RunJob('honeywell,null,null,null,null,null')
        RunJob('fifththird,null,null,null,null,null')
        RunJob('verizon-ga,null,null,null,null,null')
        RunJob('morganstanley,null,null,null,null,null')
        RunJob('fda,null,null,null,null,null')
        RunJob('web-demo,null,null,null,null,null')
        RunJob('tda,null,null,null,null,null')
        RunJob('forddirect,null,null,null,null,null')
        RunJob('intuitive,null,null,null,null,null')
        RunJob('web-demo2,null,null,null,null,null')
        RunJob('statefarm,null,null,null,null,null')
        RunJob('ford,null,null,null,null,null')
        RunJob('allstate,null,null,null,null,null')
        RunJob('usaa,null,null,null,null,null')
        RunJob('ps,null,null,null,null,null')
        RunJob('nab,null,null,null,null,null')
        RunJob('mobilecloud,null,null,null,null,null')
        RunJob('rai,null,null,null,null,null')
        RunJob('preproduction,null,null,null,null,null')
        RunJob('safeway,null,null,null,null,null')
        RunJob('ups,null,null,null,null,null')
        RunJob('atc,null,null,null,null,null')
        RunJob('pge,null,null,null,null,null')
        RunJob('test-mastercard,null,null,null,null,null')
        RunJob('metlife,null,null,null,null,null')
        RunJob('cigna,null,null,null,null,null')
        RunJob('perfectosso,null,null,null,null,null')
        RunJob('wellsfargodssg,null,null,null,null,null')
        RunJob('vzw-video,null,null,null,null,null')
        RunJob('pioneer,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')
        RunJob('testing,null,null,null,null,null')

*/



    //}


}