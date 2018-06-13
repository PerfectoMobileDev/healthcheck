
properties([
        parameters([
                choice(name: 'Invoke_Parameters', choices:"Yes\nNo", description: "Do you wish to do a dry run to grab parameters?" ),
                choice(name: 'Production_Mode', choices:"No\nYes", description: "Do you wish to run on configured production clouds?" ),
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

            RunJob('branchtest,null,null,null,null,null,B7284D9E402EECA516D2504EB95FBF75911CD71D')
            RunJob('borgias,e2e-auto@perfectomobile.com,Aa123456,pmR&Dlab,null,Rndlab123,CEBA9DC67218B41C163F3AE101A21E5F53F162BE')

        } else if ("${params.Production_Mode}" == "Yes") {

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
            RunJob('ibm-cn,null,null,null,null,null')
            RunJob('citi,null,null,null,null,null')
            RunJob('kp,null,null,null,null,null')
            RunJob('tmna,null,null,null,null,null')
            RunJob('scouter,null,null,null,null,null')
            RunJob('afs,null,null,null,null,null')
            RunJob('bayer,null,null,null,null,null')
            RunJob('medtroniccrhf,null,null,null,null,null')
            RunJob('fmglobal,null,null,null,null,null')
            RunJob('deutschebank,null,null,null,null,null')
            RunJob('hcp,null,null,null,null,null')
            RunJob('avon,null,null,null,null,null')
            RunJob('anthem,null,null,null,null,null')
            RunJob('extendedstay,null,null,null,null,null')
            RunJob('aig,null,null,null,null,null')
            RunJob('citicfc,null,null,null,null,null')
            RunJob('humana,null,null,null,null,null')
            RunJob('accenture,null,null,null,null,null')
            RunJob('h2h-philips,null,null,null,null,null')
            RunJob('att-mobility,null,null,null,null,null')
            RunJob('ca,null,null,null,null,null')
            RunJob('ubs,null,null,null,null,null')
            RunJob('allinahealth,null,null,null,null,null')
            RunJob('medtronic,null,null,null,null,null')
            RunJob('newyorklife,null,null,null,null,null')
            RunJob('starwood,null,null,null,null,null')
            RunJob('citipoc,null,null,null,null,null')
            RunJob('partners,null,null,null,null,null')
            RunJob('universal,null,null,null,null,null')
            RunJob('gm,null,null,null,null,null')
            RunJob('reporting-dev,null,null,null,null,null')
            RunJob('bcbsm-mobile,null,null,null,null,null')
            RunJob('inner-active,null,null,null,null,null')
            RunJob('noctest,null,null,null,null,null')
            RunJob('wellsfargowholesale,null,null,null,null,null')
            RunJob('jhancock,null,null,null,null,null')
            RunJob('nfcu,null,null,null,null,null')
            RunJob('citighrt,null,null,null,null,null')
            RunJob('tdacc,null,null,null,null,null')
            RunJob('kpmonitoring,null,null,null,null,null')
            RunJob('accenturecoe,null,null,null,null,null')
            RunJob('optum,null,null,null,null,null')
            RunJob('gwbto,null,null,null,null,null')
            RunJob('linux-perfecto,null,null,null,null,null')
            RunJob('charter,null,null,null,null,null')
            RunJob('vzw-siot,null,null,null,null,null')
            RunJob('siriusxm,null,null,null,null,null')
            RunJob('web-staging,null,null,null,null,null')
            RunJob('ultimate,null,null,null,null,null')
            RunJob('gmfinancial,null,null,null,null,null')
            RunJob('toyota,null,null,null,null,null')
            RunJob('vzw,null,null,null,null,null')
            RunJob('wyn,null,null,null,null,null')
            RunJob('performance,null,null,null,null,null')
            RunJob('wf,null,null,null,null,null')
            RunJob('jeppesen,null,null,null,null,null')
            RunJob('CitiCardsIUT,null,null,null,null,null')
            RunJob('sprint,null,null,null,null,null')
            RunJob('paychex,null,null,null,null,null')
            RunJob('hnb,null,null,null,null,null')
            RunJob('centene,null,null,null,null,null')
            RunJob('libertymutual,null,null,null,null,null')
            RunJob('security,null,null,null,null,null')
            RunJob('visa,null,null,null,null,null')
            RunJob('vanguard,null,null,null,null,null')
            RunJob('vzw-gizmo,null,null,null,null,null')
            RunJob('rbs,null,null,null,null,null')
            RunJob('comcast,null,null,null,null,null')
            RunJob('linux-stg,null,null,null,null,null')
            RunJob('qatestlab,null,null,null,null,null')
            RunJob('keybank,null,null,null,null,null')
            RunJob('usaa-poc,null,null,null,null,null')
            RunJob('rbc-us,null,null,null,null,null')
            RunJob('reporting-staging,null,null,null,null,null')
            RunJob('system-tests,null,null,null,null,null')
            RunJob('ebay,null,null,null,null,null')
            RunJob('roomstogo,null,null,null,null,null')
            RunJob('demo,null,null,null,null,null')
            RunJob('citrix,null,null,null,null,null')
            RunJob('branchtest,null,null,null,null,null')
            RunJob('lfg,null,null,null,null,null')
            RunJob('citizens,null,null,null,null,null')
            RunJob('jcpenney,null,null,null,null,null')
            RunJob('ulta,null,null,null,null,null')
            RunJob('bofa,null,null,null,null,null')
            RunJob('ibm-ca,null,null,null,null,null')
            RunJob('kohls,null,null,null,null,null')
            RunJob('prudential,null,null,null,null,null')
            RunJob('kroger,null,null,null,null,null')
            RunJob('bmo,null,null,null,null,null')
            RunJob('nespresso,null,null,null,null,null')
            RunJob('bbt,null,null,null,null,null')
            RunJob('wfmonitor,null,null,null,null,null')
            RunJob('awarex,null,null,null,null,null')
            RunJob('amica,null,null,null,null,null')
            RunJob('allegis,null,null,null,null,null')
            RunJob('beta,null,null,null,null,null')
            RunJob('bcbsnc,null,null,null,null,null')
            RunJob('umtb,null,null,null,null,null')
            RunJob('medtronicem,null,null,null,null,null')
            RunJob('webster,null,null,null,null,null')
            RunJob('dexmedia,null,null,null,null,null')
            RunJob('nycsca,null,null,null,null,null')
            RunJob('panera,null,null,null,null,null')
            RunJob('gett-us,null,null,null,null,null')
            RunJob('devry,null,null,null,null,null')
            RunJob('adp,null,null,null,null,null')
            RunJob('bofa-staging,null,null,null,null,null')
            RunJob('directv,null,null,null,null,null')
            RunJob('ibmgbs,null,null,null,null,null')
            RunJob('privatecloud,null,null,null,null,null')
            RunJob('citrix-hosting,null,null,null,null,null')
            RunJob('visionit,null,null,null,null,null')
            RunJob('msdev,null,null,null,null,null')
            RunJob('cademo,null,null,null,null,null')
            RunJob('bsci,null,null,null,null,null')
            RunJob('espn,null,null,null,null,null')
            RunJob('ios11,null,null,null,null,null')
            RunJob('microsoft,null,null,null,null,null')
            RunJob('jnjdiabetes,null,null,null,null,null')
            RunJob('dtvstreaming,null,null,null,null,null')
            RunJob('medtronictest,null,null,null,null,null')
            RunJob('murphyoil,null,null,null,null,null')
            RunJob('ing-aus,null,null,null,null,null')
            RunJob('bellca,null,null,null,null,null')

        }

    }


}