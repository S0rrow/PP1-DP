package edu.handong.csee.pp1.dp;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import net.lifove.clami.CLAMI;

public class Main {
	
	static boolean INFO = true;
	
	public static void main(String[] args) {
		new Main().run(args);
	}

	private void run(String[] args) {
		String gitPath = "";
		try {
			gitPath = getGitRepository(args[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String gitPath = args[0];
		String arffFilePathToBeSaved = args[1];
		
		MetricCollector metricCollector = new MetricCollector(gitPath,arffFilePathToBeSaved);
		
		metricCollector.collect();
		metricCollector.saveAnArffFile();
		
		testCLAMI();
	}
	
	public String getGitRepository(String url) throws IOException, GitAPIException {
		String path = "";
		File localPath = File.createTempFile("TestGitRepository", "");
        if(!localPath.delete()) {
            throw new IOException("Could not delete temporary file " + localPath);
        }
        System.out.println("Cloning from " + url + " to " + localPath);
        try (Git result = Git.cloneRepository()
                .setURI(url)
                .setDirectory(localPath)
                .call()) {
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
	        System.out.println("Having repository: " + result.getRepository().getDirectory());
	        path = result.getRepository().getDirectory().getAbsolutePath();
	        path = path.substring(0, path.length()-5);
	        System.out.println("given return path: " + path);
        }
        return path; 
	}
	
	public void testCLAMI() {
		String bestCutoff = "50";
		String[] args = {"-f","data/new_repo.arff","-l","label","-p", "buggy", "-m", "-c", bestCutoff};

		CLAMI.main(args);
	}
}
