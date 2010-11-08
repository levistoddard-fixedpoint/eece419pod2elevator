package data;

import core.DeliveryStatus;

public class CompletedRequest {
	public int elevatorNumber;
	public int onloadFloor;
	public int offloadFloor;
	public long enterQuantum;
	public long onloadQuantum;
	public long offloadQuantum;
	public long timeConstraint;
	public DeliveryStatus deliveryStatus;
}
