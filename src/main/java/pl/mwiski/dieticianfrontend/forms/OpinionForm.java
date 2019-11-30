package pl.mwiski.dieticianfrontend.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.Data;
import pl.mwiski.dieticianfrontend.clients.opinion.OpinionDto;
import pl.mwiski.dieticianfrontend.clients.opinion.OpinionService;
import pl.mwiski.dieticianfrontend.views.MainView;

@Data
public class OpinionForm extends FormLayout {

    private TextField opinion = new TextField("Opinion");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Binder<OpinionDto> binder = new Binder<>(OpinionDto.class);
    private OpinionService opinionService;
    private MainView mainView;
    private long opinionId;

    public OpinionForm(MainView mainView) {
        this.mainView = mainView;
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(opinion, buttons);
        binder.bind(opinion, "opinion");
        save.addClickListener(event -> save());
        delete.addClickListener(event -> delete());
    }

    private void save() {
        OpinionDto opinionDto = binder.getBean();
        opinionService.edit(opinionDto.getId(), opinion.getValue());
        mainView.refresh();
        setOpinion(null);
    }

    private void delete() {
        OpinionDto opinionDto = binder.getBean();
        opinionService.delete(opinionDto.getId());
        mainView.refresh();
        setOpinion(null);
    }

    public void setOpinion(OpinionDto opinionDto) {
        binder.setBean(opinionDto);

        if (opinionDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }
}
