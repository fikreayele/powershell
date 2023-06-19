pipelineJob('FACETSDBS/FACETSDBS_Pl/FACETSDBS_Upgrade_Pl') {

    //
    // Description
    //
    def sDescription = '''FACETSDBS Database Upgrade Pipeline'''

    description(sDescription)

    //
    // Parameters
    //
    parameters {

        // Environment
        def envDesc = '''Environment to update'''
        choiceParam('Environment', ['none'
                                   ,'tst'
                                   ,'dev'
                                   ,'trn'
                                   ,'its'
                                   ,'fst'
                                   ,'sys'
                                   ,'cfg'
                                   ], envDesc)

        // Ps1File
        def ps1FileDesc = '''PS1 File'''
        activeChoiceReactiveParam('Ps1File') {

            description(ps1FileDesc)

            choiceType('SINGLE_SELECT')

            groovyScript {

                script('''

if (Environment.equals("tst")) {
    return [
           "facetsUpgradeTstWrp"
           ]
} else if (Environment.equals("dev") ||
           Environment.equals("trn") ||
           Environment.equals("its") ||
           Environment.equals("fst") ||
           Environment.equals("sys") ||
           Environment.equals("cfg")
          ) {
    return [
           "facetsUpgradeWrp"
           ]
} else {
    return ["none"]
}
''')

                fallbackScript('return [""]')
            }
            referencedParameter('Environment')
        }

	//
	// NoUpdate
        def updDesc = 'NoUpdate - dryrun option'
        booleanParam('NoUpdate', false, updDesc)

        //
        // FacetsUpgrade Src Copy
        def facetsupgr_desc = '<font color="blue">./Facets.Upgrade to D:/DBSAnsibleXfer/Facets.Upgrade</font>'
        booleanParam('FacetsUpgradeSrcCopy', false, facetsupgr_desc)

        // Version
        def versionDesc = '<b>Facets Upgrade Version</b>'
        choiceParam('Version', ['none'
                               ,'6.00.002.000'
                               ,'6.00.003.000'
                               ], versionDesc)

        //  Facets
        def FacetsDesc = '''Facets'''
        activeChoiceReactiveParam('Facets') {

            description(FacetsDesc)

            choiceType('SINGLE_SELECT')

            groovyScript {

                script('''
if (Environment.equals("tst")) {
    return [
           "vmslcfacdba01"
           ]
} else if (Environment.equals("dev")) {
    return [
           "facdev1"
           ]
} else if (Environment.equals("trn")) {
    return [
           "factrn1"
           ]
} else if (Environment.equals("its")) {
    return [
           "facits1"
           ]
} else if (Environment.equals("fst")) {
    return [
           "facfst1"
           ]
} else if (Environment.equals("sys")) {
    return [
           "facsys1"
           ]
} else if (Environment.equals("cfg")) {
    return [
           "faccfg1"
           ]
} else {
    return ["none"]
}
''')

                fallbackScript('return [""]')
            }
            referencedParameter('Environment')
        }

        //  MiniApps
        def MiniAppsDesc = '''MiniApps'''
        activeChoiceReactiveParam('MiniApps') {

            description(MiniAppsDesc)

            choiceType('SINGLE_SELECT')

            groovyScript {

                script('''
if (Environment.equals("tst")) {
    return [
           "vmslcfacdba02"
           ]
} else if (Environment.equals("dev")) {
    return [
           "facdev1"
           ]
} else if (Environment.equals("trn")) {
    return [
           "factrn1"
           ]
} else if (Environment.equals("its")) {
    return [
           "facits1"
           ]
} else if (Environment.equals("fst")) {
    return [
           "mapfst1"
           ]
} else if (Environment.equals("sys")) {
    return [
           "mapsys1"
           ]
} else if (Environment.equals("cfg")) {
    return [
           "mapcfg1"
           ]
} else {
    return ["none"]
}
''')

                fallbackScript('return [""]')
            }
            referencedParameter('Environment')
        }

        //
        // get.facets_version (facets)
        def get_facets_version_desc = '<font color="blue">get.facets_version.sql (facets)</font>'
        booleanParam('get_facets_version', false, get_facets_version_desc)

        //
        // set.restricted (facets)
        def set_restricted_desc = '<font color="blue">set.restricted.sql (facets)</font>'
        booleanParam('set_restricted', false, set_restricted_desc)

        //
        // pre_updatedb_cdcq_cr (miniapps)
        def pre_updatedb_cdcq_cr_desc = '<font color="blue">updatedb_cdcq_cr.sql (miniapps)</font>'
        booleanParam('pre_updatedb_cdcq_cr', false, pre_updatedb_cdcq_cr_desc)

        //
        // pre_updatedb_cdcq_in (miniapps)
        def pre_updatedb_cdcq_in_desc = '<font color="blue">updatedb_cdcq_in.sql (miniapps)</font>'
        booleanParam('pre_updatedb_cdcq_in', false, pre_updatedb_cdcq_in_desc)

        //
        // pre_updatedb_cdcq_xc (miniapps)
        def pre_updatedb_cdcq_xc_desc = '<font color="blue">updatedb_cdcq_xc.sql (miniapps)</font>'
        booleanParam('pre_updatedb_cdcq_xc', false, pre_updatedb_cdcq_xc_desc)

        //
        // pre.fac (facets)
        def pre_fac_desc = '<font color="blue">pre.fac.sql (facets)</font>'
        booleanParam('pre_fac', false, pre_fac_desc)

        //
        // post.fac (facets)
        def post_fac_desc = '<font color="blue">post.fac.sql (facets)</font>'
        booleanParam('post_fac', false, post_fac_desc)

        //
        // post.map (miniapps)
        def post_map_desc = '<font color="blue">post.map.sql (miniapps)</font>'
        booleanParam('post_map', false, post_map_desc)

        //
        // validate.cdcq (miniapps)
        def validate_cdcq_desc = '<font color="blue">validate.cdcq.sql (miniapps)</font>'
        booleanParam('validate_cdcq', false, validate_cdcq_desc)

        //
        // set.multiuser (facets)
        def set_multiuser_desc = '<font color="blue">set.multiuser.sql (facets)</font>'
        booleanParam('set_multiuser', false, set_multiuser_desc)

        //
        // supplemental (facets)
        def supplemental_desc = '<font color="blue">supplemental.mql (facets)</font>'
        booleanParam('supplemental', false, supplemental_desc)

        //
        // supplemental_VERIFICATIONSCRIPT (facets)
        def supplemental_VERIFICATIONSCRIPT_desc = '<font color="blue">supplemental_VERIFICATIONSCRIPT.mql (facets)</font>'
        booleanParam('supplemental_VERIFICATIONSCRIPT', false, supplemental_VERIFICATIONSCRIPT_desc)

        //
        // Taxonomy (facets)
        def Taxonomy_desc = '<font color="blue">Taxonomy.mql (facets)</font>'
        booleanParam('Taxonomy', false, Taxonomy_desc)

        //
        // Taxonomy_VERIFICATIONSCRIPT (facets)
        def Taxonomy_VERIFICATIONSCRIPT_desc = '<font color="blue">Taxonomy_VERIFICATIONSCRIPT.mql (facets)</font>'
        booleanParam('Taxonomy_VERIFICATIONSCRIPT', false, Taxonomy_VERIFICATIONSCRIPT_desc)
  
        //
        // post_updatedb_cdcq_cr (miniapps)
        def post_updatedb_cdcq_cr_desc = '<font color="blue">updatedb_cdcq_cr.sql (miniapps)</font>'
        booleanParam('post_updatedb_cdcq_cr', false, post_updatedb_cdcq_cr_desc)

        //
        // post_updatedb_cdcq_in (miniapps)
        def post_updatedb_cdcq_in_desc = '<font color="blue">updatedb_cdcq_in.sql (miniapps)</font>'
        booleanParam('post_updatedb_cdcq_in', false, post_updatedb_cdcq_in_desc)

        //
        // post_updatedb_cdcq_xc (miniapps)
        def post_updatedb_cdcq_xc_desc = '<font color="blue">updatedb_cdcq_xc.sql (miniapps)</font>'
        booleanParam('post_updatedb_cdcq_xc', false, post_updatedb_cdcq_xc_desc)

        //
        // fix.cdcq_gap (miniapps)
        def fix_cdcq_gap_desc = '<font color="blue">fix.cdcq_gap.sql (miniapps)</font>'
        booleanParam('fix_cdcq_gap', false, fix_cdcq_gap_desc)

        //
        // ephi_view_refresh (miniapps)
        def ephi_view_refresh_desc = '<font color="blue">ephi_view_refresh.sql (miniapps)</font>'
        booleanParam('ephi_view_refresh', false, ephi_view_refresh_desc)

    }

    //
    // Definitions
    //
    definition {

        cps {
            script('''
import org.jenkinsci.plugins.pipeline.modeldefinition.Utils

@NonCPS
def getBuildUser() {
    return currentBuild.rawBuild.getCause(Cause.UserIdCause).getUserName()
}

//
// runFacetsUpgradeSrcCopy
//
def runFacetsUpgradeSrcCopy(String buser,
                            Boolean noupdate) {

    //
    runstep = String.valueOf('');

    //
    stage (stageDesc) {

        if (exeStep == true) {

            echo "Running: BuildUser: ${buser}; NoUpdate: ${noupdate}"

            build job: 'FACETSDBS/FACETSDBS_Utils/FACETSDBS_FacetsUpgradeSrcCopy', parameters:
            [
                [$class: 'BooleanParameterValue', name: 'NoUpdate',    value: noupdate]
            ]
            sleepTime(2, noupdate);
        } else {
            echo "Ignoring: BuildUser: ${buser}; NoUpdate: ${noupdate}"
            Utils.markStageSkippedForConditional(STAGE_NAME)
        }
    }
}

//
// runExeCmd
//
def runExeCmd(String buser,
              String environment,
              String ps1file,
              String version,
              String srvname,
              String sqlfile,
              Boolean noupdate) {

    //
    runstep = String.valueOf('');

    //
    stage (stageDesc) {

        if (exeStep == true) {

            echo "Running: BuildUser: ${buser}; Environment: ${environment}; NoUpdate: ${noupdate}"

            build job: 'FACETSDBS/FACETSDBS_Utils/FACETSDBS_Upgrade_ExeCmd', parameters:
            [
                [$class: 'StringParameterValue',  name: 'Environment', value: environment],
                [$class: 'StringParameterValue',  name: 'Ps1File',     value: ps1file],
                [$class: 'StringParameterValue',  name: 'Version',     value: version],
                [$class: 'StringParameterValue',  name: 'SrvName',     value: srvname],
                [$class: 'StringParameterValue',  name: 'SqlFile',     value: sqlfile],
                [$class: 'BooleanParameterValue', name: 'NoUpdate',    value: noupdate]
            ]
            sleepTime(5, noupdate);
        } else {
            echo "Ignoring: BuildUser: ${buser}; Environment: ${environment}; NoUpdate: ${noupdate}"
            Utils.markStageSkippedForConditional(STAGE_NAME)
        }
    }
}

//
// sleepTime
//
def sleepTime(int value, noupdate) {

    //
    if (noupdate == true) {
        echo "NoUpdate: Ignoring sleeping for ${value} secs."
    } else {
        echo "Sleepting for ${value} secs."
        sleep value
    }
}

//
// Globals
//
buser = getBuildUser();
environment = String.valueOf(params.Environment)
ps1file     = String.valueOf(params.Ps1File)
version     = String.valueOf(params.Version)
facets      = String.valueOf(params.Facets)
miniapps    = String.valueOf(params.MiniApps)
noupdate    = Boolean.valueOf(params.NoUpdate)

//
// Set-Tags
//
node {
   stage ('Set Tags') {
       manager.addShortText("Username: ${buser}; ${environment}; NoUpdate(${noupdate})")
   }
}

//
// FacetsUpgradeSrcCopy
//
stageDesc   = String.valueOf('FacetsUpgradeSrcCopy')
exeStep     = Boolean.valueOf(params.FacetsUpgradeSrcCopy);
runFacetsUpgradeSrcCopy(buser, noupdate)

//
// get_facets_version (facets)
//
stageDesc   = String.valueOf('get_facets_version')
exeStep     = Boolean.valueOf(params.get_facets_version);
runExeCmd(buser, environment, ps1file, version, facets, "get.facets_version.sql", noupdate)

//
// set_restricted (facets)
//
stageDesc   = String.valueOf('set_restricted')
exeStep     = Boolean.valueOf(params.set_restricted);
runExeCmd(buser, environment, ps1file, version, facets, "set.restricted.sql", noupdate)


//
// pre_updatedb_cdcq_cr (miniapps)
//
stageDesc   = String.valueOf('pre_updatedb_cdcq_cr')
exeStep     = Boolean.valueOf(params.pre_updatedb_cdcq_cr);
runExeCmd(buser, environment, ps1file, version, miniapps, "updatedb_cdcq_cr.sql", noupdate)

//
// pre_updatedb_cdcq_in (miniapps)
//
stageDesc   = String.valueOf('pre_updatedb_cdcq_in')
exeStep     = Boolean.valueOf(params.pre_updatedb_cdcq_in);
runExeCmd(buser, environment, ps1file, version, miniapps, "updatedb_cdcq_in.sql", noupdate)

//
// pre_updatedb_cdcq_xc (miniapps)
//
stageDesc   = String.valueOf('pre_updatedb_cdcq_xc')
exeStep     = Boolean.valueOf(params.pre_updatedb_cdcq_xc);
runExeCmd(buser, environment, ps1file, version, miniapps, "updatedb_cdcq_xc.sql", noupdate)


//
// pre_fac (facets)
//
stageDesc   = String.valueOf('pre_fac')
exeStep     = Boolean.valueOf(params.pre_fac);
runExeCmd(buser, environment, ps1file, version, facets, "pre.fac.sql", noupdate)

//
// post_fac (facets)
//
stageDesc   = String.valueOf('post_fac')
exeStep     = Boolean.valueOf(params.post_fac);
runExeCmd(buser, environment, ps1file, version, facets, "post.fac.sql", noupdate)

//
// post_map (miniapps)
//
stageDesc   = String.valueOf('post_map')
exeStep     = Boolean.valueOf(params.post_map);
runExeCmd(buser, environment, ps1file, version, miniapps, "post.map.sql", noupdate)

//
// validate_cdcq (miniapps)
//
stageDesc   = String.valueOf('validate_cdcq')
exeStep     = Boolean.valueOf(params.validate_cdcq);
runExeCmd(buser, environment, ps1file, version, miniapps, "validate.cdcq.sql", noupdate)

//
// set_multiuser (facets)
//
stageDesc   = String.valueOf('set_multiuser')
exeStep     = Boolean.valueOf(params.set_multiuser);
runExeCmd(buser, environment, ps1file, version, facets, "set.multiuser.sql", noupdate)

//
// supplemental (facets)
//
stageDesc   = String.valueOf('supplemental')
exeStep     = Boolean.valueOf(params.supplemental);
runExeCmd(buser, environment, ps1file, version, facets, "supplemental.mql", noupdate)

//
// supplemental_VERIFICATIONSCRIPT (facets)
//
stageDesc   = String.valueOf('supplemental_VERIFICATIONSCRIPT')
exeStep     = Boolean.valueOf(params.supplemental_VERIFICATIONSCRIPT);
runExeCmd(buser, environment, ps1file, version, facets, "supplemental_VERIFICATIONSCRIPT.mql", noupdate)

//
// Taxonomy (facets)
//
stageDesc   = String.valueOf('Taxonomy')
exeStep     = Boolean.valueOf(params.Taxonomy);
runExeCmd(buser, environment, ps1file, version, facets, "Taxonomy.mql", noupdate)

//
// Taxonomy_VERIFICATIONSCRIPT (facets)
//
stageDesc   = String.valueOf('Taxonomy_VERIFICATIONSCRIPT')
exeStep     = Boolean.valueOf(params.Taxonomy_VERIFICATIONSCRIPT);
runExeCmd(buser, environment, ps1file, version, facets, "Taxonomy_VERIFICATIONSCRIPT.mql", noupdate)

//
// post_updatedb_cdcq_cr (miniapps)
//
stageDesc   = String.valueOf('post_updatedb_cdcq_cr')
exeStep     = Boolean.valueOf(params.post_updatedb_cdcq_cr);
runExeCmd(buser, environment, ps1file, version, miniapps, "updatedb_cdcq_cr.sql", noupdate)

//
// post_updatedb_cdcq_in (miniapps)
//
stageDesc   = String.valueOf('post_updatedb_cdcq_in')
exeStep     = Boolean.valueOf(params.post_updatedb_cdcq_in);
runExeCmd(buser, environment, ps1file, version, miniapps, "updatedb_cdcq_in.sql", noupdate)

//
// post_updatedb_cdcq_xc (miniapps)
//
stageDesc   = String.valueOf('post_updatedb_cdcq_xc')
exeStep     = Boolean.valueOf(params.post_updatedb_cdcq_xc);
runExeCmd(buser, environment, ps1file, version, miniapps, "updatedb_cdcq_xc.sql", noupdate)

//
// fix_cdcq_gap (miniapps)
//
stageDesc   = String.valueOf('fix_cdcq_gap')
exeStep     = Boolean.valueOf(params.fix_cdcq_gap);
runExeCmd(buser, environment, ps1file, version, miniapps, "fix.cdcq_gap.sql", noupdate)

//
// ephi_view_refresh (miniapps)
//
stageDesc   = String.valueOf('ephi_view_refresh')
exeStep     = Boolean.valueOf(params.ephi_view_refresh);
runExeCmd(buser, environment, ps1file, version, miniapps, "ephi_view_refresh.sql", noupdate)

            '''.stripIndent())
            sandbox(false)
        }
    }
}
