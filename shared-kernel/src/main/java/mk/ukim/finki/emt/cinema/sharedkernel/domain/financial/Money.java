package mk.ukim.finki.emt.cinema.sharedkernel.domain.financial;

import lombok.Getter;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.base.ValueObject;
import mk.ukim.finki.emt.cinema.sharedkernel.domain.financial.enumeration.Currency;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
@Getter
public class Money implements ValueObject {

    @Enumerated(EnumType.STRING)
    private final Currency currency;

    private final Integer amount;

    protected Money() {
        this.currency = Currency.MKD;
        this.amount = 0;
    }

    private Money(@NonNull Currency currency, @NonNull Integer amount) {
        Objects.requireNonNull(currency, "currency must not be null");
        Objects.requireNonNull(amount, "amount must not be null");

        if (amount < 0) {
            throw new IllegalArgumentException("The amount must not be negative");
        }

        this.currency = currency;
        this.amount = amount;
    }

    public static Money valueOf(Currency currency, Integer amount) {
        return new Money(currency, amount);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies.");
        }

        return new Money(this.currency, this.amount + other.amount);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies");
        }

        return new Money(this.currency, this.amount - other.amount);
    }

    public Money multiply(Integer q) {
        return new Money(this.currency, this.amount * q);
    }

    public Boolean isBiggerThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies");
        }

        return this.amount > other.amount;
    }

    public Boolean isSmallerThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies");
        }

        return this.amount < other.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return currency == money.currency &&
                amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    @Override
    public String toString() {
        return "Money{" +
                "currency=" + currency +
                ", amount=" + amount +
                '}';
    }

}
