
properties([
        parameters([
                choice(name: 'Invoke_Parameters', choices:"Yes\nNo", description: "Do you wish to do a dry run to grab parameters?" ),
                choice(name: 'Production_Mode', choices:"No\nYes\nYes_Custom_List", description: "Do you wish to run on configured production clouds?" ),
                text(defaultValue: '', description: '', name: 'deviceBlackList'),
                text(defaultValue: 'BOS', description: '', name: 'siteWhitelist'),
                string(defaultValue: '50', description: '', name: 'maxParallelDevicesToRunOnCloud')

        ])
])

def RunJob = {cloudParams ->
    try {
        stage(cloudParams.split(',')[0]) {

            def jobName = 'deviceHealthCheckWIFI-Tokens'

            if ("${params.Production_Mode}" == "Yes") {

                jobName = 'deviceHealthCheckWIFI-Tokens'

            }

            def job = build(job: jobName, propagate: false, wait: false,
                    parameters:
                            [text(name: 'mcmParams', value: cloudParams),
                             text(name: 'deviceBlackList', value: "${params.deviceBlackList}"),
                             text(name: 'siteWhitelist', value: "${params.siteWhitelist}"),
                             string(name: 'maxParallelDevicesToRunOnCloud', value: "${params.maxParallelDevicesToRunOnCloud}")
                            ])

        }
    } catch (e) {
        currentBuild.result = 'UNSTABLE'
        echo e.toString()

    }
}

    stage("Parameterizing") {

        if ("${params.Invoke_Parameters}" == "Yes") {
            currentBuild.result = 'ABORTED'
            error('DRY RUN COMPLETED. JOB PARAMETERIZED.')
        }

    }

node('generic-slaves') {
    timeout(time: 2, unit: 'HOURS') {


        if ("${params.Production_Mode}" == "No") {

            RunJob('bbt,null,null,null,null')


        } else if ("${params.Production_Mode}" == "Yes") {

            RunJob('bbt,null,null,null,null')
            RunJob('morganstanley,null,null,null,null')
            RunJob('allegis,null,null,null,null')
            RunJob('supervalu,null,null,null,null')
            RunJob('citi,null,null,null,null')
            RunJob('avon,null,null,null,null')
            RunJob('bmo,null,null,null,null')
            RunJob('ruv,null,null,null,null')
            RunJob('accenture,null,null,null,null')
            RunJob('pnc,null,null,null,null')
            RunJob('adtalem,null,null,null,null')
            RunJob('humana,null,null,null,null')
            RunJob('gwbto,null,null,null,null')
            RunJob('accenturecoe,null,null,null,null')
            RunJob('salesforce,null,null,null,null')
            RunJob('bcbsnc,null,null,null,null')
            RunJob('aig,null,null,null,null')
            RunJob('adidas,null,null,null,null')
            RunJob('ca,null,null,null,null')
            RunJob('vanguard,null,null,null,null')
            RunJob('allstate,null,null,null,null')
            RunJob('amica,null,null,null,null')
            RunJob('citizens,null,null,null,null')
            RunJob('lfg,null,null,null,null')
            RunJob('bayer,null,null,null,null')
            RunJob('aeo,null,null,null,null')
            RunJob('nab,null,null,null,null')
            RunJob('afs,null,null,null,null')
            RunJob('kroger,null,null,null,null')
            RunJob('anthem,null,null,null,null')
            RunJob('asurion,null,null,null,null')
            RunJob('keybank,null,null,null,null')
            RunJob('demo,null,null,null,null')
            RunJob('hcsc,null,null,null,null')
            RunJob('bcbsm-mobile,null,null,null,null')
            RunJob('allinahealth,null,null,null,null')
            RunJob('atc,null,null,null,null')
            RunJob('bofa,null,null,null,null')
            RunJob('mobilecloud,null,null,null,null')
            RunJob('abi,null,null,null,null')
            RunJob('adp,null,null,null,null')
            RunJob('usaa-poc,null,null,null,null')
            RunJob('bofa-staging,null,null,null,null')
            RunJob('usaa,null,null,null,null')
            RunJob('att-mobility,null,null,null,null')
            RunJob('ally,null,null,null,null')
            RunJob('mc,null,null,null,null')
            RunJob('nespresso,null,null,null,null')
            RunJob('mbna,null,null,null,null')
            RunJob('sap,null,null,null,null')
            RunJob('beta,null,null,null,null')
            RunJob('igm,null,null,null,null')
            RunJob('centris,null,null,null,null')
            RunJob('acn,null,null,null,null')
            RunJob('allstatetest,null,null,null,null')
            RunJob('uob,null,null,null,null')
            RunJob('santander-mx,null,null,null,null')
            RunJob('mub,null,null,null,null')
            RunJob('mubtest,null,null,null,null')
            RunJob('nabqb,null,null,null,null')
            RunJob('tmo,null,null,null,null')
            RunJob('aetnahealth,null,null,null,null')
            RunJob('mnvikings,null,null,null,null')
            RunJob('att-uet,null,null,null,null')
            RunJob('newday,null,null,null,null')
            RunJob('rogers,null,null,null,null')
            RunJob('vzw-gizmo,null,null,null,null')
            RunJob('adient,null,null,null,null')
            RunJob('ofilab,null,null,null,null')
            RunJob('krogerclt,null,null,null,null')


        } else if ("${params.Production_Mode}" == "Yes_Custom_List") {

            //RunJob('tda,null,null,null,null,null')
            //RunJob('gm,null,null,null,null,null')
            //RunJob('vzw,null,null,null,null,null') // canceled
            //RunJob('mobilecloud,null,null,null,null,null') // less then 50 devices
            //RunJob('nab,null,null,null,null,null') // less then 50 devices
            //RunJob('demo,null,null,null,null,null')
            //RunJob('wf,null,null,null,null,null') // less then 50 devices
            //RunJob('mastercard,null,null,null,null,null') // less then 50 devices
            //RunJob('cigna,null,null,null,null,null')
            //RunJob('bofa,null,null,null,null,null')
            //RunJob('allstate,null,null,null,null,null')
            //RunJob('kp,null,null,null,null,null') // less then 50 devices
            //RunJob('citi,null,null,null,null,null')
            //RunJob('statefarm,null,null,null,null,null')
            //RunJob('gmfinancial,null,null,null,null,null')
            //RunJob('ford,null,null,null,null,null') // less then 50 devices
            //RunJob('citrix,null,null,null,null,null') // less then 50 devices


        }

    }


}