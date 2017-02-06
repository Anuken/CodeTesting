package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class WorldMeshes implements RenderableProvider{
	private final Iterable<Mesh> meshes;
	private final Material material = new Material();
	
	public WorldMeshes(Iterable<Mesh> meshes){
		this.meshes = meshes;
	}
	
	@Override
	public void getRenderables (Array<Renderable> renderables, Pool<Renderable> pool) {
		for (Mesh mesh : meshes) {
			Renderable renderable = pool.obtain();
			renderable.material = material;
			renderable.meshPart.mesh = mesh;
			renderable.meshPart.offset = 0;
			renderable.meshPart.size = mesh.getNumVertices();
			renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
			renderables.add(renderable);
		}
	}
}
