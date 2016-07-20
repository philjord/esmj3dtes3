package esmj3dtes3.ai;

import javax.vecmath.Vector3f;

import esmj3d.ai.PathGridInterface;
import esmj3d.ai.PathGridPathway;
import esmj3dtes3.data.records.PGRD;

public class Tes3PathGrid implements PathGridInterface
{
	private PGRD pgrd;

	public Tes3PathGrid(PGRD pgrd)
	{
		this.pgrd = pgrd;
	}

	@Override
	public Vector3f getNearestNode(Vector3f from)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathGridPathway getPathway(Vector3f from)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
