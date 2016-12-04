package esmj3dtes3.j3d.cell;

import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.J3DBuffer;
import org.jogamp.java3d.LineArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.shader.Cube;
import org.jogamp.vecmath.Vector3f;

import esmj3dtes3.data.records.PGRD;
import esmj3dtes3.data.records.PGRD.PathPoint;
import tools3d.utils.PhysAppearance;
import tools3d.utils.Utils3D;

public class J3dPGRD extends Group
{
	public J3dPGRD(PGRD pgrd)
	{
		//System.out.println("J3dPGRD " + pgrd.PGRP.pathNodes.size());
		int connOffset = 0;
		// now add a line array for the paths themselves 
		LineArray lineArray = new LineArray(pgrd.PGRC.paths.length * 2,
				LineArray.COORDINATES | GeometryArray.BY_REFERENCE | GeometryArray.USE_NIO_BUFFER);
		float[] coords = new float[pgrd.PGRC.paths.length * 3 * 2];

		// cubes for the nodes shall we say			
		for (int i = 0; i < pgrd.PGRP.pathNodes.size(); i++)
		{
			PathPoint pp = pgrd.PGRP.pathNodes.get(i);
			Vector3f p = pp.node;

			TransformGroup transformGroup = new TransformGroup();
			Transform3D transform = new Transform3D();
			transform.set(p);// note p already nif'ed
			transformGroup.setTransform(transform);

			this.addChild(transformGroup);
			transformGroup.addChild(new Cube(0.2));

			//now add connection line, note 2 lines for each, so wide path should be possible somehow
			for (int c = 0; c < pp.connectionNum; c++)
			{
				int c0 = pgrd.PGRC.paths[connOffset + c];
				//System.out.println("connecting " + i + " with " + c0);

				Vector3f p0 = pgrd.PGRP.pathNodes.get(c0).node;

				coords[(connOffset + c) * 3 * 2 + 0] = p.x;
				coords[(connOffset + c) * 3 * 2 + 1] = p.y + 0.2f;
				coords[(connOffset + c) * 3 * 2 + 2] = p.z;
				coords[(connOffset + c) * 3 * 2 + 3] = p0.x;
				coords[(connOffset + c) * 3 * 2 + 4] = p0.y + 0.2f;
				coords[(connOffset + c) * 3 * 2 + 5] = p0.z;

			}

			connOffset += pp.connectionNum;
		}

		lineArray.setCoordRefBuffer(new J3DBuffer(Utils3D.makeFloatBuffer(coords)));

		Shape3D lineShape = new Shape3D();
		lineShape.setGeometry(lineArray);
		lineShape.setAppearance(PhysAppearance.makeAppearance());

		addChild(lineShape);

	}

}
