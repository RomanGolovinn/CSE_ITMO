package interfaces;

import place.Place;

public interface Mobile {
    public void moveTo(Place out);
    public void moveTo(Place out, Place in);
}
