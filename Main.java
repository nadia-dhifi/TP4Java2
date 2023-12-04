package JAVA;
class ResourceManager {
    private boolean resourceInUse = false;

    public synchronized void acquireResource() {
        while (resourceInUse) {
            try {
                wait(); // Attendre tant que la ressource est en cours d'utilisation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        resourceInUse = true;
    }

    public synchronized void releaseResource() {
        resourceInUse = false;
        notify(); // Notifier les threads en attente que la ressource est disponible
    }
}

class ResourceUser extends Thread {
    private ResourceManager resourceManager;

    public ResourceUser(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public void run() {
        resourceManager.acquireResource();

        // Effectuer une opération sur la ressource (par exemple, imprimer le thread ID)
        System.out.println("Thread " + Thread.currentThread().getId() + " effectue une opération sur la ressource.");

        resourceManager.releaseResource();
    }
}

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager();

        // Créer et démarrer plusieurs threads ResourceUser
        for (int i = 0; i < 5; i++) {
            ResourceUser resourceUser = new ResourceUser(resourceManager);
            resourceUser.start();
        }
    }
}
