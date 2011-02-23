package de.pure.diva.arf.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;

import arf.util.Cleaner;

public class DiVAMetaModelCleanAction extends DiVAMetaModelAction {

  @Override
  protected void run(IProgressMonitor monitor, Resource vmodel) throws Exception {
    monitor.beginTask("Clean reasoning results", IProgressMonitor.UNKNOWN);
    Cleaner cleaner = new Cleaner();
    cleaner.clean(vmodel);
  }

}
