# liferay-sca

## Configuration

Check out the `liferay-sca-packages` repository to a local directory.

Take a look at `config.properties` and create a `config-ext.properties` with 
your custom values. In particular, you'll probably need to take change the 
following properties:
* `****.src.code`: The path to the source code to scan
* `****.dependabot.package.dir`: The path the `liferay-sca-packages` repository 
and where you want to save the scan's output.

The scan additional projects, add the following properties:

    ****.src.code=
    ****.dependabot.package.dir=
    ****.git.pack.ref=
    ****.ignored.folders=

where `****` is a prefix of your choice. Add this prefix to the `projects` property.

## To Run

    ant run

or to scan a single project:

    ant run -Dproject=<project>


where `<project>` is the prefix of your project from the configuration file.

After the program runs, the scan's result will be saved to your local 
`liferay-sca-packages` repository. Commit these files and push the changes to 
GitHub. On GitHub, the SCA results will be available at 
https://github.com/lr-whitehat/liferay-sca-packages/security/dependabot


> [!WARNING]
> The Sonatype OSS scan hasn't been used in a long time because it was 
producing a large number of false positives. It may or may not work currently.
