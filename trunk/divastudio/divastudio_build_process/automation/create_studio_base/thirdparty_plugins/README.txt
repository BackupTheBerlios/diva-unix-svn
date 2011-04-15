This plugin contains the Feature Modeling Plugin (FMP) available at http://gsd.uwaterloo.ca/~shshe/ca.uwaterloo.gp.fmp_0.7.0.jar

This was originally developed by the Generative Software Development Lab at University of Waterloo, Canada.

This plugin was adapted to be compatible with the fmp2diva plugin. Problems regarding the deployment of this plugin were discovered which needed correcting.

The corrections made include:

Modifications to the manifest to export the packages: ca.uwaterloo.gp.fmp.util, ca.uwaterloo.gp.fmp, ca.uwaterloo.gp.fmp.system

Re-located the class files within the jar so they are now contained within the root of the jar (and not the /bin). This caused problems with classes not being found. 