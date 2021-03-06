package search.mcts.finalmoveselection;

import java.util.concurrent.ThreadLocalRandom;

import search.mcts.nodes.BaseNode;
import util.Move;

/**
 * Selects move corresponding to the child with the highest average score
 * 
 * @author Dennis Soemers
 */
public final class MaxAvgScore implements FinalMoveSelectionStrategy 
{
	
	//-------------------------------------------------------------------------

	@Override
	public Move selectMove(final BaseNode rootNode)
	{
		int bestIdx = -1;
        double maxAvgScore = Double.NEGATIVE_INFINITY;
        int numBestFound = 0;
        
        final int numChildren = rootNode.numLegalMoves();
        final int mover = rootNode.contextRef().state().mover();
        
        for (int i = 0; i < numChildren; ++i) 
        {
        	final BaseNode child = rootNode.childForNthLegalMove(i);
        	final double avgScore;
        	
        	if (child == null)
        		avgScore = rootNode.valueEstimateUnvisitedChildren(mover, rootNode.contextRef().state());
        	else
        		avgScore = child.averageScore(mover, rootNode.contextRef().state());
        	
            if (avgScore > maxAvgScore)
            {
            	maxAvgScore = avgScore;
                bestIdx = i;
                numBestFound = 1;
            }
            else if (avgScore == maxAvgScore && 
            		ThreadLocalRandom.current().nextInt() % ++numBestFound == 0)
            {
            	bestIdx = i;
            }
        }
        
        return rootNode.nthLegalMove(bestIdx);
	}
	
	//-------------------------------------------------------------------------
	
	@Override
	public void customize(final String[] inputs)
	{
		// do nothing
	}

	//-------------------------------------------------------------------------

}
