package view.gui.managerView;

import model.item.Client;
import model.manager.ClientManager;
import model.sorting.ClientSortBy;
import view.gui.itemDialog.ClientDialog;

public class ClientManagerView extends AbstractManagerView<Client> {
    public ClientManagerView() {
        super(ClientManager.getInstance(), "Client", ClientSortBy.values());
    }

    @Override
    protected Client newItemPopUp(int idCounter) {
        ClientDialog dialog = new ClientDialog(this, getManager().getIdCounter(), null);
        return dialog.getClient();
    }

    @Override
    protected Client editItemPopUp(Client originalItem) {
        ClientDialog dialog = new ClientDialog(this, originalItem.getId(), originalItem);
        return dialog.getClient();
    }
}
