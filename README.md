# liferay-sca Github Actions

## The goal

This tool is a forked github repository and can automatically run with Github Actions scheduled on every Monday, but can be run manually also. When running it clones the required git repositories to a temporary Ubuntu server enviroment.
Also setup java17 and install 'ant' then run the forked liferay-sca tool according to the modified config-ext-properties.

## How to setup

Check the yml file that contains the automatic steps: https://github.com/tomibiro/liferay-sca-for-Github-Actions/blob/yml_file_update/.github/workflows/scan_3rdParty_vulnerabilities.yml

Some line with details:
   - schedule: as a cron job it starts on every Monday.
   - steps:
      - name: the name of the steps (free form)
      - uses: uses the built-in Action
      - with: parameters

   it is possible to add more steps.


## To Run

1, Click on the Actions in the github repository: https://github.com/tomibiro/liferay-sca-for-Github-Actions/actions
   You can see the already done runs here.

2, Click on the Workflows: https://github.com/tomibiro/liferay-sca-for-Github-Actions/actions/workflows/scan_3rdParty_vulnerabilities.yml

3, To start a new (manual run) workflow, click on 'run workflow'. Choose the branch and run it.

4, A new Workflow has started. Sometimes the page does not refresh so you need to manually refresh the page and check the newly appeared one.

5, Click on the new Workflow (Weekly Security Scan)

6, Click on the Run name: 'Security-scan'. You will see the running steps that are involved in the yml file.

7, It is possible to click into the steps for further information (logs, details)

## Future developement possibilities

- Liferay gmail does not allow to send emails with API codes by GitHub Actions, so I used my personal account
  
- This repository is a forked private so I can not use the Github Pages for publishing the report on the web
