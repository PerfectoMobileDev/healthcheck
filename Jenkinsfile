
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

            def jobName = 'deviceHealthCheckWIFI-Prod'

            if ("${params.Production_Mode}" == "Yes" || "${params.Production_Mode}" == "Yes_Custom_List") {

                jobName = 'deviceHealthCheckWIFI-Prod'

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

            RunJob('branchtest,null,null,null,null,null')


        } else if ("${params.Production_Mode}" == "Yes") {

            RunJob('libertymutual,null,null,null,null,null')
            RunJob('pepper,null,null,null,null,null')
            RunJob('borgias,null,null,null,null,null')
            RunJob('heb,null,null,null,null,null')
            RunJob('calottery,null,null,null,null,null')
            RunJob('vodacom-za,null,null,null,null,null')
            RunJob('web-staging,null,null,null,null,null')
            RunJob('ee,null,null,null,null,null')
            RunJob('scouter,null,null,null,null,null')
            RunJob('citimobilemonitoring,null,null,null,null,null')
            RunJob('dsg,null,null,null,null,null')
            RunJob('jeppesen,null,null,null,null,null')
            RunJob('cobatest,null,null,null,null,null')
            RunJob('citicardsiut,null,null,null,null,null')
            RunJob('test-directv,null,null,null,null,null')
            RunJob('starwood,null,null,null,null,null')
            RunJob('umtb,null,null,null,null,null')
            RunJob('gmfinancial,null,null,null,null,null')
            RunJob('rbc-us,null,null,null,null,null')
            RunJob('ebay,null,null,null,null,null')
            RunJob('fordtest,null,null,null,null,null')
            RunJob('hnb,null,null,null,null,null')
            RunJob('wellsfargowholesale,null,null,null,null,null')
            RunJob('becumonitoring,null,null,null,null,null')
            RunJob('servicensw,null,null,null,null,null')
            RunJob('jnjdiabetes,null,null,null,null,null')
            RunJob('coba,null,null,null,null,null')
            RunJob('nycsca,null,null,null,null,null')
            RunJob('lg-online,null,null,null,null,null')
            RunJob('awarex,null,null,null,null,null')
            RunJob('ford,null,null,null,null,null')
            RunJob('vodafone-tss-de,null,null,null,null,null')
            RunJob('ps,null,null,null,null,null')
            RunJob('coles,null,null,null,null,null')
            RunJob('msces,null,null,null,null,null')
            RunJob('citipoc,null,null,null,null,null')
            RunJob('ups,null,null,null,null,null')
            RunJob('becu,null,null,null,null,null')
            RunJob('universal,null,null,null,null,null')
            RunJob('ets-db,null,null,null,null,null')
            RunJob('pioneer,null,null,null,null,null')
            RunJob('extendedstay,null,null,null,null,null')
            RunJob('directv,null,null,null,null,null')
            RunJob('gm,null,null,null,null,null')
            RunJob('bellca,null,null,null,null,null')
            RunJob('bsci,null,null,null,null,null')
            RunJob('vzw,null,null,null,null,null')
            RunJob('ing-aus,null,null,null,null,null')
            RunJob('gett-us,null,null,null,null,null')
            RunJob('citrix-hosting,null,null,null,null,null')
            RunJob('gett,null,null,null,null,null')
            RunJob('fulcrumww,null,null,null,null,null')
            RunJob('wyn,null,null,null,null,null')
            RunJob('medtroniccrhf,null,null,null,null,null')
            RunJob('honeywell,null,null,null,null,null')
            RunJob('cibc-rbss,null,null,null,null,null')
            RunJob('tescobank,null,null,null,null,null')
            RunJob('centene,null,null,null,null,null')
            RunJob('rai,null,null,null,null,null')
            RunJob('pingidentity,null,null,null,null,null')
            RunJob('citibanamex,null,null,null,null,null')
            RunJob('tdacc,null,null,null,null,null')
            RunJob('optum,null,null,null,null,null')
            RunJob('vzw-video,null,null,null,null,null')
            RunJob('singaporeairlines,null,null,null,null,null')
            RunJob('sisal,null,null,null,null,null')
            RunJob('medtronic,null,null,null,null,null')
            RunJob('toyota,null,null,null,null,null')
            RunJob('pkobp,null,null,null,null,null')
            RunJob('jlr,null,null,null,null,null')
            RunJob('ford-ch,null,null,null,null,null')
            RunJob('frb,null,null,null,null,null')
            RunJob('huskyenergy,null,null,null,null,null')
            RunJob('security,null,null,null,null,null')
            RunJob('choicehotels,null,null,null,null,null')
            RunJob('reporting-dev,null,null,null,null,null')
            RunJob('wfmonitor,null,null,null,null,null')
            RunJob('media-saturn,null,null,null,null,null')
            RunJob('vengine-1,null,null,null,null,null')
            RunJob('partners,null,null,null,null,null')
            RunJob('dfw-directv,null,null,null,null,null')
            RunJob('cigna,null,null,null,null,null')
            RunJob('citrix-xm,null,null,null,null,null')
            RunJob('opticiptest,null,null,null,null,null')
            RunJob('nfcu,null,null,null,null,null')
            RunJob('vzw-siot,null,null,null,null,null')
            RunJob('cma-cgm,null,null,null,null,null')
            RunJob('ultimate,null,null,null,null,null')
            RunJob('ford-eu,null,null,null,null,null')
            RunJob('kpmonitoring,null,null,null,null,null')
            RunJob('flyfrontier,null,null,null,null,null')
            RunJob('reporting-staging,null,null,null,null,null')
            RunJob('qapm,null,null,null,null,null')
            RunJob('espn,null,null,null,null,null')
            RunJob('sprint,null,null,null,null,null')
            RunJob('cnrail,null,null,null,null,null')
            RunJob('comcast,null,null,null,null,null')
            RunJob('test-mastercard,null,null,null,null,null')
            RunJob('metlife,null,null,null,null,null')
            RunJob('wf,null,null,null,null,null')
            RunJob('inner-active,null,null,null,null,null')
            RunJob('sanofi,null,null,null,null,null')
            RunJob('ubs,null,null,null,null,null')
            RunJob('mastercard,null,null,null,null,null')
            RunJob('microsoft,null,null,null,null,null')
            RunJob('tmna,null,null,null,null,null')
            RunJob('mcm-auto02,null,null,null,null,null')
            RunJob('visa,null,null,null,null,null')
            RunJob('db-pwcc,null,null,null,null,null')
            RunJob('web-demo,null,null,null,null,null')
            RunJob('ikea,null,null,null,null,null')
            RunJob('iscostco,null,null,null,null,null')
            RunJob('privatecloud,null,null,null,null,null')
            RunJob('siriusxm,null,null,null,null,null')
            RunJob('visionit,null,null,null,null,null')
            RunJob('deutschebank,null,null,null,null,null')
            RunJob('qatestlab,null,null,null,null,null')
            RunJob('vodafone-uc,null,null,null,null,null')
            RunJob('whitbread,null,null,null,null,null')
            RunJob('medtronictest,null,null,null,null,null')
            RunJob('intuitive,null,null,null,null,null')
            RunJob('branchtest,null,null,null,null,null')
            RunJob('manulife-au,null,null,null,null,null')
            RunJob('cademo,null,null,null,null,null')
            RunJob('hpbch,null,null,null,null,null')
            RunJob('ibm-cn,null,null,null,null,null')
            RunJob('pge,null,null,null,null,null')
            RunJob('medtronicem,null,null,null,null,null')
            RunJob('kohls,null,null,null,null,null')
            RunJob('manulife,null,null,null,null,null')
            RunJob('lloyds-af,null,null,null,null,null')
            RunJob('vodafone-uk,null,null,null,null,null')
            RunJob('vha,null,null,null,null,null')
            RunJob('rabobank,null,null,null,null,null')
            RunJob('ing-mb,null,null,null,null,null')
            RunJob('census,null,null,null,null,null')
            RunJob('slb,null,null,null,null,null')
            RunJob('lhibm,null,null,null,null,null')
            RunJob('rbs,null,null,null,null,null')
            RunJob('bttv,null,null,null,null,null')
            RunJob('kp,null,null,null,null,null')
            RunJob('northerntrust,null,null,null,null,null')
            RunJob('bmo-qaed,null,null,null,null,null')
            RunJob('charter,null,null,null,null,null')
            RunJob('fmglobal,null,null,null,null,null')
            RunJob('citighrt,null,null,null,null,null')
            RunJob('sky,null,null,null,null,null')
            RunJob('ibm-ca,null,null,null,null,null')
            RunJob('vzw-nno,null,null,null,null,null')
            RunJob('msdev,null,null,null,null,null')
            RunJob('webster,null,null,null,null,null')
            RunJob('lloyds,null,null,null,null,null')
            RunJob('tda,null,null,null,null,null')
            RunJob('mytestmobilebyopen,null,null,null,null,null')
            RunJob('paychex,null,null,null,null,null')
            RunJob('ibm-cibc,null,null,null,null,null')
            RunJob('roomstogo,null,null,null,null,null')
            RunJob('tangerinetest,null,null,null,null,null')
            RunJob('citrix,null,null,null,null,null')
            RunJob('citicfc,null,null,null,null,null')
            RunJob('murphyusa,null,null,null,null,null')
            RunJob('forddirect,null,null,null,null,null')
            RunJob('fifththird,null,null,null,null,null')
            RunJob('royallondon,null,null,null,null,null')
            RunJob('verizon-ga,null,null,null,null,null')
            RunJob('ulta,null,null,null,null,null')
            RunJob('laquinta,null,null,null,null,null')
            RunJob('nielsen,null,null,null,null,null')
            RunJob('rbc-ca,null,null,null,null,null')
            RunJob('newyorklife,null,null,null,null,null')
            RunJob('ibmgbs,null,null,null,null,null')
            RunJob('fda,null,null,null,null,null')
            RunJob('jcpenney,null,null,null,null,null')
            RunJob('wellsfargodssg,null,null,null,null,null')
            RunJob('h2h-philips,null,null,null,null,null')
            RunJob('jhancock,null,null,null,null,null')
            RunJob('etihad,null,null,null,null,null')
            RunJob('statefarm,null,null,null,null,null')
            RunJob('prudential,null,null,null,null,null')
            RunJob('dexmedia,null,null,null,null,null')
            RunJob('panera,null,null,null,null,null')
            RunJob('ultramobile,null,null,null,null,null')
            RunJob('capgroup,null,null,null,null,null')
            RunJob('elevate,null,null,null,null,null')
            RunJob('safeway,null,null,null,null,null')
            RunJob('ybsgroup,null,null,null,null,null')
            RunJob('hcp,null,null,null,null,null')
            RunJob('performance,null,null,null,null,null')
            RunJob('system-tests,null,null,null,null,null')
            RunJob('poc-de,null,null,null,null,null')
            RunJob('medtronicdiabetes,null,null,null,null,null')
            RunJob('dtvstreaming,null,null,null,null,null')



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