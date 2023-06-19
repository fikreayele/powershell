freeStyleJob("FACETSDBS/FACETSDBS_Utils/FACETSDBS_Upgrade_ExeCmd") {

    //
    // Description
    //
    def sDescription = '''FACETSDBS Upgrade Execute CMD'''

    description(sDescription)

    //
    // Allows Jenkins to schedule and execute multiple builds concurrently.
    //
    //concurrentBuild()

    //
    // Parameters
    //
    parameters {

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

        def ps1FileDesc = '''PS1 File'''
        activeChoiceReactiveParam('Ps1File') {

            description(ps1FileDesc)

            choiceType('SINGLE_SELECT')

            groovyScript {

                script('''

if (Environment.equals("tst")) {
    return ["none"
           ,"facetsUpgradeTstWrp"
           ]
} else if (Environment.equals("dev") ||
           Environment.equals("trn") ||
           Environment.equals("its") ||
           Environment.equals("fst") ||
           Environment.equals("sys") ||
           Environment.equals("cfg")
          ) {
    return ["none"
           ,"facetsUpgradeWrp"
           ]
} else {
    return ["none"]
}
''')

                fallbackScript('return [""]')
            }
            referencedParameter('Environment')
        }

        def SrvNameDesc = '''SrvName'''
        activeChoiceReactiveParam('SrvName') {

            description(SrvNameDesc)

            choiceType('SINGLE_SELECT')

            groovyScript {

                script('''
if (Environment.equals("tst")) {
    return ["none"
           ,"vmslcfacdba01"
           ,"vmslcfacdba02"
           ]
} else if (Environment.equals("dev")) {
    return ["none"
           ,"facdev1"
           ]
} else if (Environment.equals("trn")) {
    return ["none"
           ,"factrn1"
           ]
} else if (Environment.equals("its")) {
    return ["none"
           ,"facits1"
           ]
} else if (Environment.equals("fst")) {
    return ["none"
           ,"facfst1"
           ,"mapfst1"
           ]
} else if (Environment.equals("sys")) {
    return ["none"
           ,"facsys1"
           ,"mapsys1"
           ]
} else if (Environment.equals("cfg")) {
    return ["none"
           ,"faccfg1"
           ,"mapcfg1"
           ]
} else {
    return ["none"]
}
''')

                fallbackScript('return [""]')
            }
            referencedParameter('Environment')
        }

        // Version
        def versionDesc = '<b>Facets Upgrade Version</b>'
        choiceParam('Version', ['none',
                               ,'6.00.002.000'
                               ,'6.00.003.000'
                               ], versionDesc)

        def SqlFileDesc = '''SqlFile Argument'''
        choiceParam('SqlFile', ['none'
                               ,'get.facets_version.sql'
                               ,'set.restricted.sql'
                               ,'updatedb_cdcq_cr.sql'
                               ,'updatedb_cdcq_in.sql'
                               ,'updatedb_cdcq_xc.sql'                               
                               ,'pre.fac.sql'
                               ,'post.fac.sql'
                               ,'post.map.sql'
                               ,'validate.cdcq.sql'
                               ,'set.multiuser.sql'
                               ,'supplemental.mql'
                               ,'supplemental_VERIFICATIONSCRIPT.mql'
                               ,'Taxonomy.mql'
                               ,'Taxonomy_VERIFICATIONSCRIPT.mql'
                               ,'fix.cdcq_gap.sql'
                               ,'ephi_view_refresh.sql'
                               ], SqlFileDesc)

	def updDesc = 'NoUpdate - dryrun option'
	booleanParam('NoUpdate', false, updDesc)
    }

    //
    // Git
    //
    scm {
        git {
            remote {
                name('git.cambiahealth.com')
                url('git@git.cambiahealth.com:dba/dbs-facets-upgrade-utils.git')
                credentials('gitlab_sshkey')
            }
            branch('*/develop')
        }
    }

    //
    // Wrappers
    //
    wrappers {

	// Vault passwords
        credentialsBinding {
            file('FACETS_DEV_VAULT_PASSWD', 'FACETS_DEV_VAULT_PASSWD')
            file('FACETS_TRN_VAULT_PASSWD', 'FACETS_TRN_VAULT_PASSWD')
            file('FACETS_ITS_VAULT_PASSWD', 'FACETS_ITS_VAULT_PASSWD')
            file('FACETS_FST_VAULT_PASSWD', 'FACETS_FST_VAULT_PASSWD')
            file('FACETS_SYS_VAULT_PASSWD', 'FACETS_SYS_VAULT_PASSWD')
            file('FACETS_CFG_VAULT_PASSWD', 'FACETS_CFG_VAULT_PASSWD')
        }

	// Set jenkins user build variables
	buildUserVars()

        // Adds timestamps to the console log
        timestamps()
    }

    //
    // Steps
    //
    steps {

        def Bash = '''
echo "Pushing update to ${Environment} Environment"

if [[ ${Environment} == "none" ]]; then
    echo "ERROR: Environment was not selected"
    exit 1;
fi
if [[ ${Version} == "none" ]]; then
    echo "ERROR: Version was not selected"
    exit 1;
fi
if [[ ${Ps1File} == "none" ]]; then
    echo "ERROR: Ps1File was not selected"
    exit 1;
fi

inventory="./fac${Environment}inv"

if [[ $Environment == "tst" ]]; then
    Environment=dev
    vault="${FACETS_DEV_VAULT_PASSWD}"
elif [[ $Environment == "dev" ]]; then
    vault="${FACETS_DEV_VAULT_PASSWD}"
elif [[ $Environment == "trn" ]]; then
    vault="${FACETS_TRN_VAULT_PASSWD}"
elif [[ $Environment == "its" ]]; then
    vault="${FACETS_ITS_VAULT_PASSWD}"
elif [[ $Environment == "fst" ]]; then
    vault="${FACETS_FST_VAULT_PASSWD}"
elif [[ $Environment == "sys" ]]; then
    vault="${FACETS_SYS_VAULT_PASSWD}"
elif [[ $Environment == "cfg" ]]; then
    vault="${FACETS_CFG_VAULT_PASSWD}"
fi

command="ansible-playbook ./playbooks/dbsexecmd.yml
         --inventory $inventory
         --limit utils
         --tags \\"filecopy,filecopydebug,ps1exe,ps1exedebug\\"
         --extra-vars \\"ps1file=${Ps1File}\\"
         --extra-vars \\"env=${Environment} srv=${SrvName} sql=${Version}/${SqlFile}\\""

## noupdate option
if [ ${NoUpdate} == true ]; then
    command="${command} --extra-vars noupdate=true"
else
    command="${command} --extra-vars noupdate=false"
fi

command="${command} --vault-password-file ${vault}"

## Log the AnsiblePlaybook Command
echo ${command}

##
## Execute the AnsiblePlaybook Command
##
if [ ${NoUpdate} == false ]; then
    eval ${command}
fi
'''
        shell(Bash)
    }

    //
    // Publishers
    //
    publishers {

	def gpb = '''userid =  manager.envVars['BUILD_USER'];
manager.addShortText("Username: ${userid}; Environment: ${manager.build.buildVariables.get('Environment')}; Ps1File: ${manager.build.buildVariables.get('Ps1File')}; Version: ${manager.build.buildVariables.get('Version')}; SrvName: ${manager.build.buildVariables.get('SrvName')}; SqlFile: ${manager.build.buildVariables.get('SqlFile')}; NoUpdate(${manager.build.buildVariables.get('NoUpdate')})")'''

        groovyPostBuild(gpb, Behavior.DoNothing)
    }
}
