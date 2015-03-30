  checkMark.animate().alpha(0f);
  bounds = new TransitionSet();
  bounds.setOrdering(TransitionSet.ORDERING_TOGETHER);
  bounds.addTransition(new ChangeImageTransform())
        .addTransition(new ChangeBounds());;
  set = new TransitionSet();
  set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
  set.addTransition(bounds).addTransition(new Slide(Gravity.BOTTOM));
  set.excludeTarget(R.id.checkMark, true);
  TransitionManager.go(scene2, set);