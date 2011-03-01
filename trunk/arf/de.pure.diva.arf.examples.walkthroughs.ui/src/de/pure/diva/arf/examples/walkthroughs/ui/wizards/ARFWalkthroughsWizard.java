package de.pure.diva.arf.examples.walkthroughs.ui.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import de.pure.diva.arf.examples.walkthroughs.ui.Activator;

public class ARFWalkthroughsWizard extends Wizard implements INewWizard {

  public ARFWalkthroughsWizard() {}

  protected String[] getProjectDescriptor() {
    String[] descriptor = new String[] { "zips/de.pure.diva.arf.examples.walkthroughs.zip", "de.pure.diva.arf.examples.walkthroughs" };
    return descriptor;
  }

  protected Plugin getContainerPlugin() {
    return Activator.getDefault();
  }

  @Override
  public boolean performFinish() {
    final String[] descriptor = getProjectDescriptor();
    WorkspaceJob job = new WorkspaceJob("Unzipping Projects") {
      @Override
      public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Unzipping Projects", 1);
        unzipProject(descriptor, monitor);
        monitor.worked(1);
        return Status.OK_STATUS;
      }
    };
    job.setRule(ResourcesPlugin.getWorkspace().getRoot());
    job.schedule();
    return true;
  }

  private void unzipProject(String[] descriptor, IProgressMonitor monitor) {
    String zipLocation = descriptor[0];
    String projectName = descriptor[1];

    URL interpreterZipUrl = FileLocator.find(getContainerPlugin().getBundle(), new Path(zipLocation), null);
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    if (project.exists() == false) {
      try {
        // We make sure that the project is created from this point forward.
        project.create(monitor);

        ZipInputStream zipFileStream = new ZipInputStream(interpreterZipUrl.openStream());
        ZipEntry zipEntry = zipFileStream.getNextEntry();

        // We derive a regexedProjectName so that the dots don't end up being
        // interpreted as the dot operator in the regular expression language.
        String regexedProjectName = projectName.replaceAll("\\.", "\\."); //$NON-NLS-1$ //$NON-NLS-2$

        while (zipEntry != null) {
          // We will construct the new file but we will strip off the project
          // directory from the beginning of the path because we have already
          // created the destination project for this zip.
          File file = new File(project.getLocation().toString(), zipEntry.getName().replaceFirst("^" + regexedProjectName + "/", "")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

          if (false == zipEntry.isDirectory()) {

            /*
             * Copy files (and make sure parent directory exist)
             */
            File parentFile = file.getParentFile();
            if (null != parentFile && false == parentFile.exists()) {
              parentFile.mkdirs();
            }
            OutputStream os = null;

            try {
              os = new FileOutputStream(file);

              byte[] buffer = new byte[102400];
              while (true) {
                int len = zipFileStream.read(buffer);
                if (zipFileStream.available() == 0) break;
                os.write(buffer, 0, len);
              }
            }
            finally {
              if (null != os) {
                os.close();
              }
            }
          }

          zipFileStream.closeEntry();
          zipEntry = zipFileStream.getNextEntry();
        }

        project.open(monitor);
        // in order to make sure the project natures are correctly identified
        // close and reopen the project
        project.close(monitor);
        project.open(monitor);
        project.refreshLocal(IFile.DEPTH_INFINITE, monitor);
      }
      catch (IOException e) {
        getContainerPlugin().getLog().log(new Status(IStatus.ERROR, getContainerPlugin().getBundle().getSymbolicName(), IStatus.ERROR, e.getMessage(), e));
      }
      catch (CoreException e) {
        getContainerPlugin().getLog().log(e.getStatus());
      }
    }
  }

  public void init(IWorkbench workbench, IStructuredSelection selection) {}

}
