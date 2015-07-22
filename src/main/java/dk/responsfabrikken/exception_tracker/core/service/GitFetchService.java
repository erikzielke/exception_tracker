package dk.responsfabrikken.exception_tracker.core.service;

import dk.responsfabrikken.exception_tracker.core.model.client.CodeDto;
import dk.responsfabrikken.exception_tracker.core.model.server.ExceptionGroup;
import dk.responsfabrikken.exception_tracker.core.model.server.Project;
import dk.responsfabrikken.exception_tracker.core.model.server.ProjectRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class GitFetchService {
    @Autowired ProjectRepository projectRepository;

    public void fetchRepositories() {
        File repositoriesRoot = new File("C:\\exception_tracker");
        List<Project> projectList = projectRepository.findAll();
        for (Project project : projectList) {
            if (project.getRepositoryUrl() != null) {
                File file = new File(repositoriesRoot, project.getName());
                if (!file.exists()) {
                    try {
                        Git.cloneRepository().setURI(project.getRepositoryUrl()).setDirectory(file).call();
                    } catch (GitAPIException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Git git = new Git(new FileRepository(new File(file, ".git")));
                        git.fetch().call();
                    } catch (IOException | GitAPIException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public String getCode(ExceptionGroup one, String path) {
        try {
            File repositoriesRoot = new File("C:\\exception_tracker");
            File workingDir = new File(repositoriesRoot, one.getProject().getName());
            FileRepository repo = new FileRepository(new File(workingDir, ".git"));
            Git git = new Git(repo);
            Repository repository = git.getRepository();
            ObjectId lastCommitId = repository.resolve(Constants.HEAD);
            RevWalk revWalk = new RevWalk(repository);
            RevCommit commit = revWalk.parseCommit(lastCommitId);
            RevTree tree = commit.getTree();

            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(path));
            boolean found = treeWalk.next();

            if (found) {
                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader objectLoader = repository.open(objectId);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                objectLoader.copyTo(out);
                return out.toString();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
}
