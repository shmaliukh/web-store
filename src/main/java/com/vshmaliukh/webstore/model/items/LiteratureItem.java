package com.vshmaliukh.webstore.model.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import static com.vshmaliukh.webstore.ConstantsForEntities.PAGES_COLUMN;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class LiteratureItem extends Item {

    @Column(name = PAGES_COLUMN, nullable = false)
    private int pages;

    protected LiteratureItem(Integer id, String category, String name, int price, int quantity, boolean isAvailableInStore, int pages) {
        super(id, category, name, price, quantity, isAvailableInStore);
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LiteratureItem)) return false;
        if (!super.equals(o)) return false;

        LiteratureItem that = (LiteratureItem) o;

        return getPages() == that.getPages();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getPages();
        return result;
    }

}
