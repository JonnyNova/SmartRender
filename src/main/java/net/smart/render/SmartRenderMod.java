// ==================================================================
// This file is part of Smart Render.
//
// Smart Render is free software: you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Smart Render is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Smart Render. If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package net.smart.render;

import java.lang.reflect.*;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import net.smart.render.statistics.*;
import net.smart.utilities.*;

@Mod(modid = "SmartRender", name = "Smart Render", version = "2.2", clientSideOnly = true, dependencies = "required-after:PlayerAPI@[1.0,)")
public class SmartRenderMod
{
	private static boolean addRenderer = true;

	private boolean hasRenderer = false;

	public static void doNotAddRenderer()
	{
		addRenderer = false;
	}

	@EventHandler
	@SuppressWarnings("unused")
	public void init(FMLPreInitializationEvent event)
	{
		hasRenderer = Loader.isModLoaded("RenderPlayerAPI");

		if(hasRenderer)
		{
			Class<?> type = Reflect.LoadClass(SmartRenderMod.class, new Name("net.smart.render.playerapi.SmartRender"), true);
			Method method = Reflect.GetMethod(type, new Name("register"));
			Reflect.Invoke(method, null);
		}

		if(!hasRenderer && addRenderer)
			SmartRenderContext.registerRenderers(RenderPlayer.class);
	}

	@EventHandler
	@SuppressWarnings("unused")
	public void init(FMLInitializationEvent event)
	{
		net.smart.render.statistics.playerapi.SmartStatistics.register();

		net.smart.render.statistics.playerapi.SmartStatisticsFactory.initialize();

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	@SuppressWarnings({ "static-method", "unused" })
	public void tickStart(ClientTickEvent event)
	{
		SmartStatisticsContext.onTickInGame();
	}
}